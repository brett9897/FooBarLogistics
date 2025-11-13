package com.foobarlogistics.test.fakes

import com.foobarlogistics.application.ports.output.WarehouseRepository
import com.foobarlogistics.domain.warehouse.Warehouse

class InMemoryWarehouseRepository : WarehouseRepository {
    private val warehouses = mutableMapOf<String, Warehouse>()

    override fun findById(id: String): Warehouse? {
        return warehouses[id]
    }

    override fun save(warehouse: Warehouse) {
        warehouses[warehouse.id] = warehouse
    }

    fun clear() = warehouses.clear()
}