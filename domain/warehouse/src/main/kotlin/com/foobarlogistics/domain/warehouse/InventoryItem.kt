package com.foobarlogistics.domain.warehouse

data class InventoryItem(val id: Int, val sku: String, val quantity: Int) {
    companion object {
        fun newItem(sku: String) = InventoryItem(0, sku, 0)
    }
}
