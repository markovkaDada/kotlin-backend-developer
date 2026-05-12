package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId

object OrderTable : Table("swift_orders") {
    val id = text("id")
    val description = text("description")
    val amount = decimal("amount", precision = 15, scale = 2)
    val status = text("status")
    val ownerId = text("owner_id")
    val fileId = text("file_id")

    override val primaryKey = PrimaryKey(id)

    fun from(row: ResultRow) = SwiftOrder(
        id = SwiftOrderId(row[id]),
        description = row[description],
        amount = row[amount],
        status = SwiftOrderStatus.valueOf(row[status]),
        ownerId = SwiftOwnerId(row[ownerId]),
        fileId = SwiftFileId(row[fileId]),
    )
}
