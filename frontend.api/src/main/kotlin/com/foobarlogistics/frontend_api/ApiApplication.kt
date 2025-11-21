package com.foobarlogistics.frontend_api

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication(
    scanBasePackages = ["com.foobarlogistics.frontend_api", "com.foobarlogistics.infrastructure.database"],
    exclude = [
        FlywayAutoConfiguration::class
    ]
)
class ApiApplication {
    @Bean
    fun logEnvironmentVariables(env : Environment): CommandLineRunner {
        return CommandLineRunner {
            val log = LoggerFactory.getLogger(ApiApplication::class.java)
            val varsToCheck = listOf(
                "POSTGRES_USER",
                "WAREHOUSE_DB_NAME",
                "POSTGRES_HOST",
                "POSTGRES_PORT",
                "app.datasource.warehouse.jdbc-url",
                "app.datasource.warehouse.driver-class-name"
            )

            log.info("===== Checking environment variables: =====")
            varsToCheck.forEach {
                log.info("$it: ${env.getProperty(it)}")
            }
            log.info("======================================")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
