package ru.otus.otuskotlin.yan.swiftorder.repoinmemory

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderIdRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderRequest
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseErr
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrderResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.models.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class SwiftOrderRepoInMemory(
    initOrders: List<SwiftOrder> = emptyList(),
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
    val maxEntries: Int = 1000,
) : ISwiftOrderRepo {

    private val mutex = Mutex()
    private val store = ConcurrentHashMap<String, SwiftOrder>()

    init {
        initOrders.forEach { store[it.id.asString] = it }
    }

    override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse = mutex.withLock {
        if (store.size >= maxEntries)
            return@withLock DbOrderResponseErr(SOError(code = "limit-exceeded", message = "Repository limit of $maxEntries entries reached"))
        val id = randomUuid()
        val order = rq.order.copy(id = SwiftOrderId(id))
        store[id] = order
        DbOrderResponseOk(order)
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse {
        val key = rq.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return mutex.withLock {
            store[key]?.let { DbOrderResponseOk(it) }
                ?: DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
        }
    }

    override suspend fun updateOrder(rq: DbOrderRequest): DbOrderResponse {
        val key = rq.order.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return mutex.withLock {
            if (!store.containsKey(key))
                DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
            else {
                store[key] = rq.order
                DbOrderResponseOk(rq.order)
            }
        }
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse {
        val key = rq.id.asString.takeIf { it.isNotBlank() }
            ?: return DbOrderResponseErr(SOError(code = "id-empty", message = "Id must not be blank"))
        return mutex.withLock {
            store.remove(key)?.let { DbOrderResponseOk(it) }
                ?: DbOrderResponseErr(SOError(code = "not-found", message = "Order $key not found"))
        }
    }

    override suspend fun searchOrders(rq: DbOrderFilterRequest): DbOrdersResponse {
        val result = mutex.withLock {
            store.values
                .filter { order ->
                    rq.ownerIdFilter.asString.takeIf { it.isNotBlank() }
                        ?.let { it == order.ownerId.asString } ?: true
                }
                .filter { order ->
                    rq.statusFilter.takeIf { it != SwiftOrderStatus.NONE }
                        ?.let { it == order.status } ?: true
                }
                .toList()
        }
        return DbOrdersResponseOk(result)
    }
}
