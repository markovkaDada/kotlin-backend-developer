package ru.otus.otuskotlin.yan.swiftorder.appui.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

class HttpOrderClient(
    private val baseUrl: String,
    private val httpClient: HttpClient,
) : OrderClient {

    override suspend fun search(): List<SwiftOrder> {
        val response = httpClient.post("$baseUrl/v1/order/search") {
            contentType(ContentType.Application.Json)
            setBody(OrderSearchRequest())
        }.body<OrderSearchResponse>()

        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Search failed")

        return response.orders?.map { it.toSwiftOrder() } ?: emptyList()
    }

    override suspend fun read(id: String): SwiftOrder {
        val response = httpClient.post("$baseUrl/v1/order/read") {
            contentType(ContentType.Application.Json)
            setBody(OrderReadRequest(order = OrderReadObject(id = id)))
        }.body<OrderReadResponse>()

        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Read failed")

        return response.order?.toSwiftOrder() ?: throw OrderClientException("Order not found")
    }

    override suspend fun create(order: SwiftOrder): SwiftOrder {
        val response = httpClient.post("$baseUrl/v1/order/create") {
            contentType(ContentType.Application.Json)
            setBody(order.toCreateRequest())
        }.body<OrderCreateResponse>()

        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Create failed")

        return response.order?.toSwiftOrder() ?: throw OrderClientException("No order in response")
    }

    override suspend fun update(order: SwiftOrder): SwiftOrder {
        val response = httpClient.post("$baseUrl/v1/order/update") {
            contentType(ContentType.Application.Json)
            setBody(order.toUpdateRequest())
        }.body<OrderUpdateResponse>()

        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Update failed")

        return response.order?.toSwiftOrder() ?: throw OrderClientException("No order in response")
    }

    override suspend fun delete(id: String) {
        val response = httpClient.post("$baseUrl/v1/order/delete") {
            contentType(ContentType.Application.Json)
            setBody(OrderDeleteRequest(order = OrderDeleteObject(id = id)))
        }.body<OrderDeleteResponse>()

        if (response.result == ResponseResult.ERROR)
            throw OrderClientException(response.errors?.firstOrNull()?.message ?: "Delete failed")
    }
}
