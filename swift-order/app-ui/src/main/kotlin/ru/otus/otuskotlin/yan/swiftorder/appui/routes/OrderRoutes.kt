package ru.otus.otuskotlin.yan.swiftorder.appui.routes

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.yan.swiftorder.appui.client.OrderClient
import ru.otus.otuskotlin.yan.swiftorder.appui.templates.*
import ru.otus.otuskotlin.yan.swiftorder.models.*
import java.math.BigDecimal

fun Application.orderRoutes(httpClient: OrderClient, kafkaClient: OrderClient) {
    routing {

        get("/orders") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val ownerId = call.request.queryParameters["ownerId"] ?: ""
            val statusParam = call.request.queryParameters["status"]
            val status = statusParam?.runCatching { SwiftOrderStatus.valueOf(this) }?.getOrNull()
            val client = call.orderClient(httpClient, kafkaClient)
            runCatching { client.search(ownerId = ownerId, status = status) }.fold(
                onSuccess = { orders ->
                    call.respondHtml { layout("Заказы", t) { orderListPage(orders, t, ownerId, statusParam ?: "") } }
                },
                onFailure = { e ->
                    call.respondHtml { layout("Заказы", t, e.message) { orderListPage(emptyList(), t, ownerId, statusParam ?: "") } }
                }
            )
        }

        get("/orders/new") {
            val t = call.request.queryParameters["transport"] ?: "http"
            call.respondHtml { layout("Новый заказ", t) { orderFormPage(null, t) } }
        }

        post("/orders") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val p = call.receiveParameters()
            val order = SwiftOrder(
                description = p["description"] ?: "",
                amount = p["amount"]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                ownerId = SwiftOwnerId(p["ownerId"] ?: ""),
                fileId = SwiftFileId(p["fileId"] ?: ""),
            )
            runCatching { call.orderClient(httpClient, kafkaClient).create(order) }.fold(
                onSuccess = { created ->
                    call.respondRedirect("/orders/${created.id.asString}?transport=$t")
                },
                onFailure = { e ->
                    call.respondHtml { layout("Новый заказ", t, e.message) { orderFormPage(order, t) } }
                }
            )
        }

        get("/orders/{id}/edit") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val id = call.parameters["id"] ?: return@get call.respondRedirect("/orders?transport=$t")
            runCatching { call.orderClient(httpClient, kafkaClient).read(id) }.fold(
                onSuccess = { order ->
                    call.respondHtml { layout("Редактировать заказ", t) { orderFormPage(order, t) } }
                },
                onFailure = { call.respondRedirect("/orders/$id?transport=$t") }
            )
        }

        post("/orders/{id}") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val id = call.parameters["id"] ?: return@post call.respondRedirect("/orders?transport=$t")
            val p = call.receiveParameters()
            val order = SwiftOrder(
                id = SwiftOrderId(id),
                description = p["description"] ?: "",
                amount = p["amount"]?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                status = p["status"]?.runCatching { SwiftOrderStatus.valueOf(this) }?.getOrElse { SwiftOrderStatus.NEW } ?: SwiftOrderStatus.NEW,
                ownerId = SwiftOwnerId(p["ownerId"] ?: ""),
                fileId = SwiftFileId(p["fileId"] ?: ""),
            )
            runCatching { call.orderClient(httpClient, kafkaClient).update(order) }.fold(
                onSuccess = { call.respondRedirect("/orders/$id?transport=$t") },
                onFailure = { e ->
                    call.respondHtml { layout("Редактировать заказ", t, e.message) { orderFormPage(order, t) } }
                }
            )
        }

        get("/orders/{id}/delete") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val id = call.parameters["id"] ?: return@get call.respondRedirect("/orders?transport=$t")
            runCatching { call.orderClient(httpClient, kafkaClient).read(id) }.fold(
                onSuccess = { order ->
                    call.respondHtml { layout("Удалить заказ", t) { orderDeletePage(order, t) } }
                },
                onFailure = { call.respondRedirect("/orders?transport=$t") }
            )
        }

        post("/orders/{id}/delete") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val id = call.parameters["id"] ?: return@post call.respondRedirect("/orders?transport=$t")
            runCatching { call.orderClient(httpClient, kafkaClient).delete(id) }.fold(
                onSuccess = { call.respondRedirect("/orders?transport=$t") },
                onFailure = { call.respondRedirect("/orders/$id?transport=$t") }
            )
        }

        get("/orders/{id}") {
            val t = call.request.queryParameters["transport"] ?: "http"
            val id = call.parameters["id"] ?: return@get call.respondRedirect("/orders?transport=$t")
            runCatching { call.orderClient(httpClient, kafkaClient).read(id) }.fold(
                onSuccess = { order ->
                    call.respondHtml { layout("Заказ #${id.take(8)}", t) { orderDetailPage(order, t) } }
                },
                onFailure = { e ->
                    call.respondHtml { layout("Ошибка", t, e.message) { orderListPage(emptyList(), t) } }
                }
            )
        }
    }
}
