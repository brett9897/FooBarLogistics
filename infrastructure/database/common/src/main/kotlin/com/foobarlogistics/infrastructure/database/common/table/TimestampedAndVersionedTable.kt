package com.foobarlogistics.infrastructure.database.common.table

abstract class TimestampedAndVersionedTable(name : String) : TimestampedTable(name) {
    val version = long("version").default(1)
}