package com.foobarlogistics.application.commands

import com.foobarlogistics.application.dto.InventoryItemDto
import com.foobarlogistics.application.ports.input.ReceiveInventoryUseCase
import com.foobarlogistics.application.ports.output.WarehouseRepository
import com.foobarlogistics.application.dto.WarehouseDto
import com.foobarlogistics.domain.warehouse.InventoryOperations
import java.time.Instant

class InventoryCommand(
    private val warehouseRepository: WarehouseRepository
): ReceiveInventoryUseCase {
    override fun receiveInventory(now: Instant, warehouseId: String, productSku: String, units: Int) {
        val warehouseDto =
            warehouseRepository.findByName(warehouseId)
            ?: throw WarehouseNotFoundException("Warehouse with id = $warehouseId not found")

        val warehouse = WarehouseDto.toDomain(warehouseDto)
        val event = InventoryOperations.receiveInventory(now, warehouse, productSku, units)

        val eventInventory = event.warehouse.getInventory().map { InventoryItemDto.fromDomain(it) }
        val deletedInventory = warehouseDto.inventory.filter { it.sku !in eventInventory.map { item -> item.sku } }

        val newWarehouseDto = warehouseDto.copy(inventory = eventInventory + deletedInventory)

        warehouseRepository.save(newWarehouseDto)
    }
}

class WarehouseNotFoundException(message: String) : RuntimeException(message)
