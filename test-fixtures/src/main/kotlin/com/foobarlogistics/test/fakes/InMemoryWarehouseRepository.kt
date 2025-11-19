package com.foobarlogistics.test.fakes

import com.foobarlogistics.application.dto.WarehouseDto
import com.foobarlogistics.application.ports.output.WarehouseRepository

class InMemoryWarehouseRepository : WarehouseRepository {
    private val warehouses = mutableMapOf<String, WarehouseDto>()

    override fun findByName(name: String): WarehouseDto? {
        return warehouses[name]
    }

    override fun save(warehouse: WarehouseDto) {
        warehouses[warehouse.name] = warehouse
    }

    fun clear() = warehouses.clear()
}