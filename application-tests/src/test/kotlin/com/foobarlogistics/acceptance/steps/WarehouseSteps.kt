package com.foobarlogistics.acceptance.steps

import io.cucumber.java.en.Given

import com.foobarlogistics.acceptance.support.TestContext
import com.foobarlogistics.application.dto.WarehouseDto

class WarehouseSteps(private val testContext: TestContext) {
    @Given("warehouse {string} exists")
    fun warehouseExists(warehouseName: String) {
        val warehouse = WarehouseDto(1, warehouseName, emptyList())
        testContext.warehouseRepository.save(warehouse)
        testContext.currentWarehouseName = warehouseName
    }
}