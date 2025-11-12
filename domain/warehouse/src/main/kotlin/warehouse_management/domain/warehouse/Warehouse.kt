package warehouse_management.domain.warehouse

data class Warehouse(
    val id: String,
    private val inventory: Map<String, Int> = emptyMap()
) {

    fun receive(productSku: String, units: Int): Warehouse {
        val newQuantity = inventory.getOrDefault(productSku, 0) + units
        return copy(inventory = inventory + (productSku to newQuantity))
    }

    fun getInventoryLevel(productSku: String): Int = inventory.getOrDefault(productSku, 0)
}
