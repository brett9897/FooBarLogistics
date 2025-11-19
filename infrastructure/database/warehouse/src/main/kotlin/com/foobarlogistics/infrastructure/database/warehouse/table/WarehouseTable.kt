package com.foobarlogistics.infrastructure.database.warehouse.table

import com.foobarlogistics.infrastructure.database.common.table.TimestampedTable

object WarehouseTable : TimestampedTable("warehouse") {
    val name = varchar("name", 50)
}