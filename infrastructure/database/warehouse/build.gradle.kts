import com.foobarlogistics.gradle.utils.EnvLoader
import com.foobarlogistics.gradle.tasks.CreateMigrationTask
import com.foobarlogistics.gradle.tasks.CreateDataBaseTask
import com.foobarlogistics.gradle.tasks.FlywayMigrateTask
import java.util.Properties

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"
    `java-library`
}

group = "com.foobarlogistics.infrastructure.database.warehouse"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.exposed.spring.starter)
    implementation(libs.datafaker)
    implementation(project(":infrastructure:database:common"))
    implementation(project(":application"))

    testImplementation(libs.kotestRunner)
    testImplementation(libs.kotestAssertions)
    testImplementation(libs.kotestTestContainers)
    testImplementation(libs.testContainers)
    testImplementation(libs.testContainers.postgresql)

    runtimeOnly(libs.postgresql.driver)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

// Load properties from both files directly. This is simple and effective.
val envProps = Properties()
envProps.putAll(EnvLoader.loadEnvProperties(rootProject.file("infrastructure/database/.env.common")))
envProps.putAll(EnvLoader.loadEnvProperties(rootProject.file("infrastructure/database/.env")))


// Task to create the database if it doesn't exist
tasks.register<CreateDataBaseTask>("createDatabase") {
    group = "database" // Optional: organize your task
    description = "Creates the warehouse database if it doesn't exist."

    // Now, just pass the file locations. This is configuration-cache-safe.
    commonEnvFile.set(rootProject.file("infrastructure/database/.env.common"))
    specificEnvFile.set(rootProject.file("infrastructure/database/.env"))
}

tasks.register<FlywayMigrateTask>("flywayMigrate") {
    group = "database"
    description = "Runs Flyway migrations on the warehouse database"

    dependsOn("createDatabase")

    val host = envProps.getProperty("POSTGRES_HOST", "localhost")
    val port = envProps.getProperty("POSTGRES_PORT", "5432")
    val dbName = envProps.getProperty("WAREHOUSE_DB_NAME", "warehouse")

    url.set("jdbc:postgresql://$host:$port/$dbName")
    user.set(envProps.getProperty("POSTGRES_USER", "postgres"))
    password.set(envProps.getProperty("POSTGRES_PASSWORD", "password"))

    migrationLocations.from(layout.projectDirectory.dir("src/main/resources/db/migrations"))
}

tasks.register<CreateMigrationTask>("createMigration") {
    group = "Migration"
    description = "Creates a new Flyway migration file. Usage: --desc=\"your description\""
    migrationsDir.set(layout.projectDirectory.dir("src/main/resources/db/migrations"))
}
