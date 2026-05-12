package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderIdRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import java.util.UUID

class SwiftOrderRepoPostgres(
    properties: SqlProperties,
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
) : ISwiftOrderRepo {

    internal val db = Database.connect(
        url = properties.url,
        driver = "org.postgresql.Driver",
        user = properties.user,
        password = properties.password,
    )

    init {
        transaction(db) {
            SchemaUtils.create(OrderTable)
        }
    }

    private suspend inline fun <T> dbTransaction(crossinline block: () -> T): T =
        withContext(Dispatchers.IO) { transaction(db) { block() } }

    override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse = try {
        dbTransaction {
            val newId = randomUuid()
            OrderTable.insert {
                it[id] = newId
                it[description] = rq.order.description
                it[amount] = rq.order.amount
                it[status] = rq.order.status.name
                it[ownerId] = rq.order.ownerId.asString
                it[fileId] = rq.order.fileId.asString
            }
            DbOrderResponseOk(rq.order.copy(id = SwiftOrderId(newId)))
        }
    } catch (e: Exception) {
        DbOrderResponseErr(SOError(code = "db-error", message = e.message ?: "DB error on create"))
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse {
        val key = rq.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return try {
            dbTransaction {
                OrderTable.selectAll().where { OrderTable.id eq key }.singleOrNull()
                    ?.let { DbOrderResponseOk(OrderTable.from(it)) }
                    ?: DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
            }
        } catch (e: Exception) {
            DbOrderResponseErr(SOError(code = "db-error", message = e.message ?: "DB error on read"))
        }
    }

    override suspend fun updateOrder(rq: DbOrderRequest): DbOrderResponse {
        val key = rq.order.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return try {
            dbTransaction {
                val updated = OrderTable.update({ OrderTable.id eq key }) {
                    it[description] = rq.order.description
                    it[amount] = rq.order.amount
                    it[status] = rq.order.status.name
                    it[ownerId] = rq.order.ownerId.asString
                    it[fileId] = rq.order.fileId.asString
                }
                if (updated == 0)
                    DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
                else
                    DbOrderResponseOk(rq.order)
            }
        } catch (e: Exception) {
            DbOrderResponseErr(SOError(code = "db-error", message = e.message ?: "DB error on update"))
        }
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse {
        val key = rq.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return try {
            dbTransaction {
                val existing = OrderTable.selectAll().where { OrderTable.id eq key }.singleOrNull()
                    ?: return@dbTransaction DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
                OrderTable.deleteWhere { id eq key }
                DbOrderResponseOk(OrderTable.from(existing))
            }
        } catch (e: Exception) {
            DbOrderResponseErr(SOError(code = "db-error", message = e.message ?: "DB error on delete"))
        }
    }

    override suspend fun searchOrders(rq: DbOrderFilterRequest): DbOrdersResponse = try {
        dbTransaction {
            val rows = OrderTable.selectAll().where {
                val conditions = buildList {
                    if (rq.ownerIdFilter.asString.isNotBlank())
                        add(OrderTable.ownerId eq rq.ownerIdFilter.asString)
                    if (rq.statusFilter != SwiftOrderStatus.NONE)
                        add(OrderTable.status eq rq.statusFilter.name)
                }
                conditions.reduceOrNull { a, b -> a and b } ?: org.jetbrains.exposed.sql.Op.TRUE
            }
            DbOrdersResponseOk(rows.map { OrderTable.from(it) })
        }
    } catch (e: Exception) {
        DbOrdersResponseErr(err = SOError(code = "db-error", message = e.message ?: "DB error on search"))
    }
}
