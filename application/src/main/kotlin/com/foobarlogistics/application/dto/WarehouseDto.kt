package com.foobarlogistics.application.dto

import com.foobarlogistics.domain.warehouse.Warehouse

data class WarehouseDto(val id: Int, val name: String, val inventory: List<InventoryItemDto>) {
    companion object {
        fun fromDomain(warehouse: Warehouse) =
            WarehouseDto(
                warehouse.id,
                warehouse.name,
                warehouse.getInventory().map { InventoryItemDto.fromDomain(it) }
            )

        fun toDomain(warehouseDto: WarehouseDto): Warehouse {
            val inventory = warehouseDto.inventory.map { InventoryItemDto.toDomain(it) }

            return Warehouse(warehouseDto.id, warehouseDto.name, inventory.associateBy { it.sku })
        }
    }
}