package warehouse_management.application.ports.input

import java.time.Instant

interface ReceiveInventoryUseCase {
    fun receiveInventory(now: Instant, warehouseId: String, productSku: String, units: Int)
}