package com.foobarlogistics.acceptance.steps

import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import io.kotest.matchers.shouldBe
import com.foobarlogistics.acceptance.support.TestContext
import java.time.Instant

class InventorySteps(private val testContext: TestContext) {
    @When("I receive {int} units of product {string}")
    fun receiveProduct(units: Int, productSku: String) {
        val now = Instant.now()
        val warehouseId = testContext.currentWarehouseId ?: "0"
        testContext.inventoryService.receiveInventory(now, warehouseId, productSku, units)
    }

    @Then("the inventory should show {int} units of {string}")
    fun verifyInventory(expectedUnits: Int, productSku: String) {
        val warehouseId = testContext.currentWarehouseId ?: "0"
        val warehouse = testContext.warehouseRepository.findById(warehouseId) ?: throw AssertionError("Warehouse $warehouseId not found")

        warehouse.getInventoryLevel(productSku) shouldBe expectedUnits
    }
}