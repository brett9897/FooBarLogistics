package com.foobarlogistics.infrastructure.database.warehouse.table

import com.foobarlogistics.infrastructure.database.common.table.TimestampedTable

object InventoryTable : TimestampedTable("inventory") {
    val warehouseId = reference("warehouse_id", WarehouseTable.id)
    val productSku = varchar("sku", 50)
    val quantity = integer("quantity")
}