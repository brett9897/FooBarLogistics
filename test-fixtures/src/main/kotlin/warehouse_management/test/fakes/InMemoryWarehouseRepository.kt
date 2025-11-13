package warehouse_management.test.fakes

import warehouse_management.application.ports.output.WarehouseRepository
import warehouse_management.domain.warehouse.Warehouse

class InMemoryWarehouseRepository : WarehouseRepository {
    private val warehouses = mutableMapOf<String, Warehouse>()

    override fun findById(id: String): Warehouse? {
        return warehouses[id]
    }

    override fun save(warehouse: Warehouse) {
        warehouses[warehouse.id] = warehouse
    }

    fun clear() = warehouses.clear()
}