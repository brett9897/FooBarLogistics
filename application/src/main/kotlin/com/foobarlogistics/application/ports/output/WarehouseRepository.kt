package com.foobarlogistics.application.ports.output

import com.foobarlogistics.application.dto.WarehouseDto

interface WarehouseRepository {
    fun findByName(name: String): WarehouseDto?
    fun save(warehouse: WarehouseDto)
}