package com.foobarlogistics.domain.warehouse

data class Warehouse(
    val id: Int,
    val name: String,
    private val inventory: Map<String, InventoryItem> = emptyMap()
) {

    fun receive(productSku: String, units: Int): Warehouse {
        val inventoryItem = inventory.getOrDefault(productSku, InventoryItem.newItem(productSku))
        return copy(
            inventory = inventory +
                    (
                        productSku to inventoryItem.copy(quantity = inventoryItem.quantity + units)
                    )
        )
    }

    fun getInventoryLevel(productSku: String): Int =
        inventory.getOrDefault(productSku, InventoryItem.newItem(productSku)).quantity

    fun getInventory(): List<InventoryItem> = inventory.values.toList()
}
