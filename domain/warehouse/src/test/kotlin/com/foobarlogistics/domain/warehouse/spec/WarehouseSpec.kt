package com.foobarlogistics.domain.warehouse.spec

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import com.foobarlogistics.domain.warehouse.Warehouse

class WarehouseSpec : BehaviorSpec({
    given("a warehouse with ID WH-001") {
        val warehouse = Warehouse(1,"WH-001")

        `when`("I check its identifier") {
            val name = warehouse.name

            then("it should be WH-001") {
                name shouldBe "WH-001"
            }
        }

        `when`("receiving 100 units of SKU-123") {
            val updatedWarehouse = warehouse.receive("SKU-123", 100)

            then("inventory should show 100 units") {
                updatedWarehouse.getInventoryLevel("SKU-123") shouldBe 100
            }

            then("original warehouse should remain unchanged") {
                warehouse.getInventoryLevel("SKU-123") shouldBe 0
            }
        }

        `when`("receiving multiple shipments of the same product") {
            val afterFirst = warehouse.receive("SKU-123", 100)
            val afterSecond = afterFirst.receive("SKU-123", 50)

            then("inventory should accumulate to 150 units") {
                afterSecond.getInventoryLevel("SKU-123") shouldBe 150
            }

            then("first warehouse should still have 100 units") {
                afterFirst.getInventoryLevel("SKU-123") shouldBe 100
            }

            then("original warehouse should still be empty") {
                warehouse.getInventoryLevel("SKU-123") shouldBe 0
            }
        }

        `when`("receiving different products") {
            val withApples = warehouse.receive("SKU-123", 100)
            val withApplesAndOranges = withApples.receive("SKU-456", 50)

            then("should track both products separately") {
                withApplesAndOranges.getInventoryLevel("SKU-123") shouldBe 100
                withApplesAndOranges.getInventoryLevel("SKU-456") shouldBe 50
            }
        }
    }

    given("a warehouse with existing inventory") {
        val warehouse = Warehouse(2,"WH-002")
            .receive("SKU-123", 100)
            .receive("SKU-456", 200)

        `when`("checking inventory levels") {
            then("should return correct levels for each product") {
                warehouse.getInventoryLevel("SKU-123") shouldBe 100
                warehouse.getInventoryLevel("SKU-456") shouldBe 200
            }
        }

        `when`("checking inventory for non-existent product") {
            then("should return zero") {
                warehouse.getInventoryLevel("SKU-999") shouldBe 0
            }
        }
    }
})