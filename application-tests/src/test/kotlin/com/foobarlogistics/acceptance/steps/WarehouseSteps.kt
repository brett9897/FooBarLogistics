package com.foobarlogistics.acceptance.steps

import io.cucumber.java.en.Given

import com.foobarlogistics.acceptance.support.TestContext
import com.foobarlogistics.domain.warehouse.Warehouse

class WarehouseSteps(private val testContext: TestContext) {
    @Given("warehouse {string} exists")
    fun warehouseExists(warehouseId: String) {
        val warehouse = Warehouse(warehouseId)
        testContext.warehouseRepository.save(warehouse)
        testContext.currentWarehouseId = warehouseId
    }
}