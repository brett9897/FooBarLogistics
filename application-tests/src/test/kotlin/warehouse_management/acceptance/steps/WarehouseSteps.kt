package warehouse_management.acceptance.steps

import io.kotest.matchers.shouldBe
import io.cucumber.java.en.Given

import warehouse_management.acceptance.support.TestContext
import warehouse_management.domain.warehouse.Warehouse

class WarehouseSteps(private val testContext: TestContext) {
    @Given("warehouse {string} exists")
    fun warehouseExists(warehouseId: String) {
        val warehouse = Warehouse(warehouseId)
        testContext.warehouseRepository.save(warehouse)
        testContext.currentWarehouseId = warehouseId
    }
}