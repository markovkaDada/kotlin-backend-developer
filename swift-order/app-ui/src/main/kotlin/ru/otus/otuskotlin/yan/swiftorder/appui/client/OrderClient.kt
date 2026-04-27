package ru.otus.otuskotlin.yan.swiftorder.appui.client

import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

interface OrderClient {
    suspend fun search(): List<SwiftOrder>
    suspend fun read(id: String): SwiftOrder
    suspend fun create(order: SwiftOrder): SwiftOrder
    suspend fun update(order: SwiftOrder): SwiftOrder
    suspend fun delete(id: String)
}

class OrderClientException(message: String, cause: Throwable? = null) : Exception(message, cause)
