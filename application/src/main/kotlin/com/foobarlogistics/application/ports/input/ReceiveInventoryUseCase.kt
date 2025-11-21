package com.foobarlogistics.application.ports.input

import java.time.Instant

interface ReceiveInventoryUseCase {
    fun receiveInventory(now: Instant, warehouseName: String, productSku: String, units: Int)
}