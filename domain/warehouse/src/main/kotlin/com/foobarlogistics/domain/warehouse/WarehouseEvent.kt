package com.foobarlogistics.domain.warehouse

import java.time.Instant

sealed interface WarehouseEvent {
    val warehouse: Warehouse
    val occurredAt: Instant
}

data class InventoryReceived(
    override val warehouse: Warehouse,
    val productSku: String,
    val quantity: Int,
    override val occurredAt: Instant
) : WarehouseEvent