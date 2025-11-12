package warehouse_management.acceptance.steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then

class InventorySteps {
    @When("I receive {int} units of product {string}")
    fun receiveProduct(units: Int, productSku: String) {
        // TODO: Call application use case to receive inventory
        println("Receiving $units units of $productSku")
    }

    @Then("the inventory should show {int} units of {string}")
    fun verifyInventory(expectedUnits: Int, productSku: String) {
        // TODO: Verify inventory level
        println("Verifying $expectedUnits units of $productSku")
    }
}