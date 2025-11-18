package com.foobarlogistics.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.sql.DriverManager
import java.util.Properties
import com.foobarlogistics.gradle.utils.EnvLoader

abstract class CreateDataBaseTask : DefaultTask() {

    // 1. Define inputs for the task
    @get:InputFile
    abstract val commonEnvFile: RegularFileProperty

    @get:InputFile
    abstract val specificEnvFile: RegularFileProperty

    // 3. Move your execution logic into a @TaskAction method
    @TaskAction
    fun createDb() {
        val props = Properties()
        // Use the input properties to get the files
        props.putAll(EnvLoader.loadEnvProperties(commonEnvFile.get().asFile))
        props.putAll(EnvLoader.loadEnvProperties(specificEnvFile.get().asFile))

        val host = props.getProperty("POSTGRES_HOST", "localhost")
        val port = props.getProperty("POSTGRES_PORT", "5432")
        val dbName = props.getProperty("WAREHOUSE_DB_NAME", "warehouse")
        val baseDbUrl = "jdbc:postgresql://$host:$port/postgres"

        val dbUsername = props.getProperty("POSTGRES_USER", "postgres")
        val dbPassword = props.getProperty("POSTGRES_PASSWORD", "password")

        // Your JDBC logic is unchanged
        DriverManager.getConnection(baseDbUrl, dbUsername, dbPassword).use { connection ->
            val stmt = connection.createStatement()
            val resultSet = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '$dbName'")
            if (!resultSet.next()) {
                logger.lifecycle("Database '$dbName' does not exist. Creating it...")
                stmt.executeUpdate("CREATE DATABASE \"$dbName\"")
                logger.lifecycle("Database '$dbName' created successfully.")
            } else {
                logger.lifecycle("Database '$dbName' already exists.")
            }
        }
    }
}