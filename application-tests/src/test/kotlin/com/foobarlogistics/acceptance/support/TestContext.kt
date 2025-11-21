package com.foobarlogistics.acceptance.support

import com.foobarlogistics.application.commands.InventoryCommands
import com.foobarlogistics.test.fakes.InMemoryWarehouseRepository

class TestContext {
    val warehouseRepository = InMemoryWarehouseRepository()
    val inventoryService = InventoryCommands(warehouseRepository)
    var currentWarehouseName: String? = null

    fun reset() {
        warehouseRepository.clear()
        currentWarehouseName = null
    }
}