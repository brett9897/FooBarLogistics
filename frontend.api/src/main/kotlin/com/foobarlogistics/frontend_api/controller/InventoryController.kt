package com.foobarlogistics.frontend_api.controller

import com.foobarlogistics.application.commands.InventoryCommands
import com.foobarlogistics.application.commands.WarehouseNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import java.time.Instant

data class ReceiveInventoryRequest(
    val sku: String,
    val quantity: Int
)

@RestController
@RequestMapping("/warehouse/{warehouseName}/inventory")
class InventoryController(private val inventoryCommands: InventoryCommands) {

    private val logger = LoggerFactory.getLogger(InventoryController::class.java)

    @PostMapping("/receive")
    fun receiveInventory(
        @PathVariable warehouseName: String,
        @RequestBody request: ReceiveInventoryRequest
    ): ResponseEntity<Any> {
        try {
            inventoryCommands.receiveInventory(
                now = Instant.now(),
                warehouseName = warehouseName,
                productSku = request.sku,
                units = request.quantity
            )

            return ResponseEntity.ok().build()
        }
        catch (notFound: WarehouseNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("error" to (notFound.message ?: "Warehouse not found.")))
        }
        catch (ex: Exception) {
            logger.error("Failed to receive inventory.", ex)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("error" to (ex.message ?: "Unknown error.")))
        }
    }
}
