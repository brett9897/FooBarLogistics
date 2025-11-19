package com.foobarlogistics.acceptance.support

import com.foobarlogistics.application.commands.InventoryCommand
import com.foobarlogistics.test.fakes.InMemoryWarehouseRepository

class TestContext {
    val warehouseRepository = InMemoryWarehouseRepository()
    val inventoryService = InventoryCommand(warehouseRepository)
    var currentWarehouseName: String? = null

    fun reset() {
        warehouseRepository.clear()
        currentWarehouseName = null
    }
}