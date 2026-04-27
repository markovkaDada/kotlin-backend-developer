package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

fun FlowContent.orderDeletePage(order: SwiftOrder, transport: String) {
    a(href = "/orders/${order.id.asString}?transport=$transport", classes = "back-link") { +"← Назад" }
    div("card") {
        h1 { +"Удалить заказ?" }
        p {
            +"Вы уверены, что хотите удалить заказ "
            b { +"#${order.id.asString.take(8)}" }
            +" — \"${order.description}\"? Это действие необратимо."
        }
        div("actions") {
            form(action = "/orders/${order.id.asString}/delete?transport=$transport",
                method = FormMethod.post) {
                submitInput(classes = "btn btn-danger") { value = "Да, удалить" }
            }
            a(href = "/orders/${order.id.asString}?transport=$transport",
                classes = "btn btn-secondary") { +"Отмена" }
        }
    }
}
