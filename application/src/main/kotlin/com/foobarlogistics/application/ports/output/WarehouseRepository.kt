package com.foobarlogistics.application.ports.output

import com.foobarlogistics.domain.warehouse.Warehouse

interface WarehouseRepository {
    fun findById(id: String): Warehouse?
    fun save(warehouse: Warehouse)
}