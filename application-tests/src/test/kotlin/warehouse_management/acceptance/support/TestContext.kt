package warehouse_management.acceptance.support

import warehouse_management.application.services.InventoryService
import warehouse_management.test.fakes.InMemoryWarehouseRepository

class TestContext {
    val warehouseRepository = InMemoryWarehouseRepository()
    val inventoryService = InventoryService(warehouseRepository)
    var currentWarehouseId: String? = null

    fun reset() {
        warehouseRepository.clear()
        currentWarehouseId = null
    }
}