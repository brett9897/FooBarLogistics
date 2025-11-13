package warehouse_management.application.services

import warehouse_management.application.ports.input.ReceiveInventoryUseCase
import warehouse_management.application.ports.output.WarehouseRepository
import warehouse_management.domain.warehouse.InventoryOperations
import java.time.Instant

class InventoryService(
    private val warehouseRepository: WarehouseRepository
) : ReceiveInventoryUseCase {
    override fun receiveInventory(now: Instant, warehouseId: String, productSku: String, units: Int) {
        val warehouse = warehouseRepository.findById(warehouseId)
        warehouse?.let {
            val event = InventoryOperations.receiveInventory(now, warehouse, productSku,units)
            warehouseRepository.save(event.warehouse)
        } ?: throw WarehouseNotFoundException("Warehouse with id = $warehouseId not found")
    }
}

class WarehouseNotFoundException(message: String) : RuntimeException(message)