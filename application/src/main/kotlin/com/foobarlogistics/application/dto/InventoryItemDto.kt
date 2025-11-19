package com.foobarlogistics.application.dto

import com.foobarlogistics.domain.warehouse.InventoryItem

sealed interface InventoryItemDto {
    val sku: String
    val quantity: Int

    companion object {
        // Default to Saved state when converting from Domain objects (usually for reading)
        fun fromDomain(inventoryItem: InventoryItem) =
            when(inventoryItem.id) {
                0 -> NewInventoryItemDto(inventoryItem.sku, inventoryItem.quantity)
                else -> SavedInventoryItemDto(inventoryItem.id, inventoryItem.sku, inventoryItem.quantity)
            }

        fun toDomain(inventoryItemDto: InventoryItemDto): InventoryItem =
            when(inventoryItemDto) {
                is NewInventoryItemDto -> InventoryItem.newItem(inventoryItemDto.sku)
                is SavedInventoryItemDto -> InventoryItem(inventoryItemDto.id, inventoryItemDto.sku, inventoryItemDto.quantity)
                is DeleteInventoryItemDto -> InventoryItem(inventoryItemDto.id, inventoryItemDto.sku, inventoryItemDto.quantity)
            }
    }
}

data class NewInventoryItemDto(
    override val sku: String,
    override val quantity: Int
) : InventoryItemDto

data class SavedInventoryItemDto(
    val id: Int,
    override val sku: String,
    override val quantity: Int
) : InventoryItemDto

data class DeleteInventoryItemDto(
    val id: Int,
    override val sku: String = "", // Optional context
    override val quantity: Int = 0 // Optional context
) : InventoryItemDto
