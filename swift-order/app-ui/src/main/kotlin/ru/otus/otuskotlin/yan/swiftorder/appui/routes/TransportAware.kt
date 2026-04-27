package ru.otus.otuskotlin.yan.swiftorder.appui.routes

import io.ktor.server.application.*
import ru.otus.otuskotlin.yan.swiftorder.appui.client.OrderClient

fun ApplicationCall.orderClient(http: OrderClient, kafka: OrderClient): OrderClient =
    if (request.queryParameters["transport"] == "kafka") kafka else http
