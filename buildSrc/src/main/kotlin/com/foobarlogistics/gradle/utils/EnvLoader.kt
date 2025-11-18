package com.foobarlogistics.gradle.utils

import java.io.File
import java.util.Properties

/**
 * A utility object to load properties from .env files.
 */
object EnvLoader {

    // This is now a public, reusable function
    fun loadEnvProperties(file: File): Properties {
        val properties = Properties()
        if (file.exists()) {
            file.readLines().forEach { line ->
                val trimmedLine = line.trim()
                if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("#")) {
                    val parts = trimmedLine.split("=", limit = 2)
                    if (parts.size == 2) {
                        val key = parts[0].trim()
                        val value = parts[1].trim().removeSurrounding("\"").removeSurrounding("'")
                        properties[key] = value
                    }
                }
            }
        }
        return properties
    }
}