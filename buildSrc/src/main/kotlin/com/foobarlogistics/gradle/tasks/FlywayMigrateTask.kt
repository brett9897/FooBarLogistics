package com.foobarlogistics.gradle.tasks

import org.flywaydb.core.Flyway
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

abstract class FlywayMigrateTask : DefaultTask() {

    @get:Input
    abstract val url: Property<String>

    @get:Input
    abstract val user: Property<String>

    @get:Input
    abstract val password: Property<String>

    @get:InputFiles
    abstract val migrationLocations: ConfigurableFileCollection

    @get:Input
    abstract val validateOnMigrate: Property<Boolean>

    init {
        // Set default value
        validateOnMigrate.convention(false)
    }

    @TaskAction
    fun migrate() {
        val locations = migrationLocations.files.map { "filesystem:${it.absolutePath}" }.toTypedArray()

        logger.lifecycle("Running Flyway migration...")
        logger.lifecycle("URL: ${url.get()}")
        logger.lifecycle("User: ${user.get()}")
        logger.lifecycle("Locations: ${locations.joinToString(", ")}")

        // Log what files exist in the migration directories
        migrationLocations.files.forEach { dir ->
            if (dir.exists() && dir.isDirectory) {
                val sqlFiles = dir.listFiles { file -> file.extension == "sql" }
                logger.lifecycle("Found ${sqlFiles?.size ?: 0} SQL files in: ${dir.absolutePath}")
                sqlFiles?.forEach { file ->
                    logger.lifecycle("  - ${file.name}")
                }
            } else {
                logger.warn("Migration location does not exist or is not a directory: ${dir.absolutePath}")
            }
        }

        val flyway = Flyway.configure()
            .dataSource(url.get(), user.get(), password.get())
            .locations(*locations)
            .validateOnMigrate(validateOnMigrate.get())
            .load()

        // Get info about migrations before running
        val info = flyway.info()
        val allMigrations = info.all()
        logger.lifecycle("Total migrations found by Flyway: ${allMigrations.size}")
        allMigrations.forEach { migration ->
            logger.lifecycle("  - ${migration.version}: ${migration.description} [${migration.state}]")
        }

        val result = flyway.migrate()

        logger.lifecycle("Flyway migration completed successfully!")
        logger.lifecycle("Migrations executed: ${result.migrationsExecuted}")
        logger.lifecycle("Target schema version: ${result.targetSchemaVersion ?: "latest"}")
        logger.lifecycle("Initial schema version: ${result.initialSchemaVersion ?: "empty"}")
        logger.lifecycle("Schema name: ${result.schemaName ?: "default"}")
    }
}