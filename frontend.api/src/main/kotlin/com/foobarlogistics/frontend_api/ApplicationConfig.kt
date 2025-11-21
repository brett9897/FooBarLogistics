package com.foobarlogistics.frontend_api

import com.foobarlogistics.application.ports.output.WarehouseRepository
import com.foobarlogistics.application.commands.InventoryCommands
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {
    @Bean
    fun inventoryCommands(warehouseRepository: WarehouseRepository): InventoryCommands =
        InventoryCommands(warehouseRepository)
}