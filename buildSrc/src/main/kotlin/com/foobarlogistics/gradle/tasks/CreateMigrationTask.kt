package com.foobarlogistics.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.util.Date

@CacheableTask
abstract class CreateMigrationTask : DefaultTask() {

    @get:Input
    @get:Option(option = "fdesc", description = "A description for the new migration file.")
    abstract val fileDescription: Property<String>

    @get:OutputDirectory
    abstract val migrationsDir: DirectoryProperty

    @TaskAction
    fun create() {
        // The 'get()' call here is safe and correct because it's inside the TaskAction.
        val desc = fileDescription.get()
        val timestamp = System.currentTimeMillis()
        val fileName = "V${timestamp}__${desc.replace(" ", "_").lowercase()}.sql"
        val migrationFile = migrationsDir.file(fileName).get().asFile

        // Ensure parent directory exists
        migrationFile.parentFile.mkdirs()

        migrationFile.writeText("""
            -- Migration: $desc
            -- Created: ${Date()}
            
            -- Add your SQL statements here
            
        """.trimIndent())

        logger.lifecycle("Created migration file: ${migrationFile.path}")
    }
}