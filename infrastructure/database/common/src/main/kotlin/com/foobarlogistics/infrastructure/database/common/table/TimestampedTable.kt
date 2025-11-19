package com.foobarlogistics.infrastructure.database.common.table

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.time.LocalDateTime

abstract class TimestampedTable(name : String) : IntIdTable(name) {
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now().toKotlinLocalDateTime() }
    val updatedAt = datetime("updated_at").nullable()
    val isDeleted = bool("is_deleted").default(false)
}