package com.foobarlogistics.acceptance.steps

import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import io.kotest.matchers.shouldBe
import com.foobarlogistics.acceptance.support.TestContext
import com.foobarlogistics.application.dto.NewInventoryItemDto
import java.time.Instant

class InventorySteps(private val testContext: TestContext) {
    @When("I receive {int} units of product {string}")
    fun receiveProduct(units: Int, productSku: String) {
        val now = Instant.now()
        val warehouseName = testContext.currentWarehouseName ?: "0"
        testContext.inventoryService.receiveInventory(now, warehouseName, productSku, units)
    }

    @Then("the inventory should show {int} units of {string}")
    fun verifyInventory(expectedUnits: Int, productSku: String) {
        val warehouseName = testContext.currentWarehouseName ?: "0"
        val warehouse = testContext.warehouseRepository.findByName(warehouseName) ?: throw AssertionError("Warehouse $warehouseName not found")
        val product = warehouse.inventory.find { it.sku == productSku } ?: NewInventoryItemDto(productSku, 0)

        product.quantity shouldBe expectedUnits
    }
}