package com.foobarlogistics.domain.warehouse.spec

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import com.foobarlogistics.domain.warehouse.*
import java.time.Instant

class InventoryOperationsSpec : BehaviorSpec({
    
    given("a warehouse") {
        val warehouse = Warehouse("WH-001")
        
        `when`("receiving inventory") {
            val event = InventoryOperations.receiveInventory(
                Instant.now(),
                warehouse,
                "SKU-123",
                100
            )
            
            then("should update warehouse state") {
                event.warehouse.id shouldBe "WH-001"
                event.warehouse.getInventoryLevel("SKU-123") shouldBe 100
            }
            
            then("should produce inventory received event") {
                event.warehouse.id shouldBe "WH-001"
                event.productSku shouldBe "SKU-123"
                event.quantity shouldBe 100
            }
            
            then("original warehouse should remain unchanged") {
                warehouse.getInventoryLevel("SKU-123") shouldBe 0
            }
        }
        
        `when`("receiving multiple shipments") {
            val event1 = InventoryOperations.receiveInventory(
                Instant.now(),
                warehouse,
                "SKU-123",
                100
            )
            val event2 = InventoryOperations.receiveInventory(
                Instant.now(),
                event1.warehouse,
                "SKU-123",
                50
            )
            
            then("should accumulate inventory") {
                event2.warehouse.getInventoryLevel("SKU-123") shouldBe 150
            }
            
            then("should produce separate events") {
                event1.quantity shouldBe 100
                event2.quantity shouldBe 50
            }
        }
    }
})