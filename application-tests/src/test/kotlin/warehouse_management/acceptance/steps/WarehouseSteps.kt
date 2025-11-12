package warehouse_management.acceptance.steps

import io.cucumber.java.en.Given

class WarehouseSteps {
    @Given("warehouse {string} exists")
    fun warehouseExists(warehouseId: String) {
        println("Setting up warehouse: $warehouseId")
    }
}