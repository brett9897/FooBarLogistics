package com.foobarlogistics.infrastructure.database.warehouse

import com.foobarlogistics.application.dto.InventoryItemDto
import com.foobarlogistics.application.dto.NewInventoryItemDto
import com.foobarlogistics.application.dto.WarehouseDto
import com.foobarlogistics.application.ports.output.WarehouseRepository
import net.datafaker.Faker
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.math.BigDecimal


@Component
@Profile("local") // Only runs in local dev
class LocalDataSeeder(private val repository: WarehouseRepository) : CommandLineRunner {
    private val faker: Faker = Faker() // Datafaker library

    override fun run(vararg args: String?) = transaction {
        if (repository.findAllNoChildren().isEmpty()) {
            val warehouses = listOf(
                WarehouseDto(0, "WH-001", emptyList()),
                WarehouseDto(0, "WH-002", emptyList()),
                WarehouseDto(0, "WH-003", emptyList())
            )

            warehouses.forEach { repository.save(it) }
        }

        val savedWarehouses = repository.findAllNoChildren().toList()

        savedWarehouses.forEach {
            if (repository.findAllInventory(it.id).isEmpty()) {
                val items: MutableList<InventoryItemDto> = ArrayList()
                val randomNumItems = faker.number().numberBetween(25, 50)
                for (i in 0..randomNumItems) {
                    items.add(
                        NewInventoryItemDto(
                            faker.code().asin(),
                            faker.number().numberBetween(1, 100)
                        )
                    )
                }

                repository.save(it.copy(inventory = items))
                println("🌱 Seeded " + items.size + " items.")
            }
        }
    }
}