package com.foobarlogistics.infrastructure.database.warehouse.repository

import com.foobarlogistics.application.dto.SavedInventoryItemDto
import com.foobarlogistics.application.dto.NewInventoryItemDto
import com.foobarlogistics.application.dto.DeleteInventoryItemDto
import com.foobarlogistics.application.dto.WarehouseDto
import com.foobarlogistics.application.ports.output.WarehouseRepository
import com.foobarlogistics.infrastructure.database.warehouse.table.WarehouseTable
import com.foobarlogistics.infrastructure.database.warehouse.table.InventoryTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.update

class ExposedWarehouseRepository : WarehouseRepository {
    override fun findByName(name: String): WarehouseDto {
        return transaction {
            val warehouseResult =
                WarehouseTable
                    .selectAll()
                    .where { WarehouseTable.name eq name }
                    .singleOrNull()
                    ?: throw NoSuchElementException("Warehouse with name $name not found")

            val inventoryItems =
                InventoryTable
                    .selectAll()
                    .where { InventoryTable.warehouseId eq warehouseResult[WarehouseTable.id] }
                    .map {
                        SavedInventoryItemDto(
                            it[InventoryTable.id].value,
                            it[InventoryTable.productSku],
                            it[InventoryTable.quantity]
                        )
                    }
            WarehouseDto(
                warehouseResult[WarehouseTable.id].value,
                warehouseResult[WarehouseTable.name],
                inventoryItems
            )
        }
    }

    override fun save(warehouse: WarehouseDto) {
        transaction {
            // 1. Upsert Warehouse
            val warehouseEntityId = if (warehouse.id == 0) {
                WarehouseTable.insertAndGetId {
                    it[name] = warehouse.name
                }
            } else {
                WarehouseTable.update({ WarehouseTable.id eq warehouse.id }) {
                    it[name] = warehouse.name
                }
                EntityID(warehouse.id, WarehouseTable)
            }

            // 2. Handle Inventory based on explicit types
            warehouse.inventory.forEach { itemDto ->
                when (itemDto) {
                    is NewInventoryItemDto -> {
                        InventoryTable.insert {
                            it[warehouseId] = warehouseEntityId
                            it[productSku] = itemDto.sku
                            it[quantity] = itemDto.quantity
                        }
                    }

                    is SavedInventoryItemDto -> {
                        InventoryTable.update({ InventoryTable.id eq itemDto.id }) {
                            it[productSku] = itemDto.sku
                            it[quantity] = itemDto.quantity
                        }
                    }

                    is DeleteInventoryItemDto -> {
                        InventoryTable.update( { InventoryTable.id eq itemDto.id }) {
                            it[InventoryTable.isDeleted] = true
                        }
                    }
                }
            }
        }
    }
}