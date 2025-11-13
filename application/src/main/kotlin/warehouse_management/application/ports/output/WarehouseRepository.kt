package warehouse_management.application.ports.output

import warehouse_management.domain.warehouse.Warehouse

interface WarehouseRepository {
    fun findById(id: String): Warehouse?
    fun save(warehouse: Warehouse)
}