package warehouse_management.domain.warehouse

import java.time.Instant

object InventoryOperations {
    fun receiveInventory(
        now: Instant,
        warehouse: Warehouse,
        productSku: String,
        quantity: Int
    ): InventoryReceived {
        val updated = warehouse.receive(productSku, quantity)
        val event = InventoryReceived(
            warehouse = updated,
            productSku = productSku,
            quantity = quantity,
            occurredAt = now
        )
        return event
    }
}