package com.foobarlogistics.application.ports.output

import com.foobarlogistics.application.dto.InventoryItemDto
import com.foobarlogistics.application.dto.WarehouseDto

interface WarehouseRepository {
    fun findByName(name: String): WarehouseDto?
    fun save(warehouse: WarehouseDto)
    fun findAllNoChildren(): List<WarehouseDto>
    fun findAllInventory(warehouseId: Int): List<InventoryItemDto>
}