package com.foobarlogistics.acceptance.support

import com.foobarlogistics.application.services.InventoryService
import com.foobarlogistics.test.fakes.InMemoryWarehouseRepository

class TestContext {
    val warehouseRepository = InMemoryWarehouseRepository()
    val inventoryService = InventoryService(warehouseRepository)
    var currentWarehouseId: String? = null

    fun reset() {
        warehouseRepository.clear()
        currentWarehouseId = null
    }
}