package com.foobarlogistics.infrastructure.database.warehouse

import org.jetbrains.exposed.sql.Database
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class WarehouseDatabaseConfig {

    @Bean("warehouseDataSource")
    @ConfigurationProperties("app.datasource.warehouse")
    open fun warehouseDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean("warehouseTransactionManager")
    open fun warehouseTransactionManager(@Qualifier("warehouseDataSource") ds: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(ds)
    }

    // Create the Exposed Database instance for Warehouse
    @Bean("warehouseDatabase")
    open fun warehouseDatabase(@Qualifier("warehouseDataSource") ds: DataSource): Database {
        return Database.connect(ds)
    }
}
