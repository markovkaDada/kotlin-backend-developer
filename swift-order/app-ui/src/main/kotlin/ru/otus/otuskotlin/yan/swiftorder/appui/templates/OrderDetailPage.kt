package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

fun FlowContent.orderDetailPage(order: SwiftOrder, transport: String) {
    a(href = "/orders?transport=$transport", classes = "back-link") { +"← Назад к списку" }
    div("card") {
        h1 { +"Заказ #${order.id.asString.take(8)}" }
        listOf(
            "ID"        to order.id.asString,
            "Описание"  to order.description,
            "Сумма"     to order.amount.toPlainString(),
            "Статус"    to order.status.name,
            "Владелец"  to order.ownerId.asString,
            "Файл"      to order.fileId.asString,
        ).forEach { (label, value) ->
            div("field-row") {
                span("field-label") { +"$label:" }
                span { +value }
            }
        }
        div("actions") {
            a(href = "/orders/${order.id.asString}/edit?transport=$transport",
                classes = "btn btn-primary") { +"Редактировать" }
            a(href = "/orders/${order.id.asString}/delete?transport=$transport",
                classes = "btn btn-danger") { +"Удалить" }
        }
    }
}
