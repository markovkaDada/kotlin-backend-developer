package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder

fun FlowContent.orderListPage(orders: List<SwiftOrder>, transport: String) {
    div("card") {
        div("row-actions") {
            h1 { +"Заказы" }
            a(href = "/orders/new?transport=$transport", classes = "btn btn-primary") { +"+ Создать заказ" }
        }
        if (orders.isEmpty()) {
            p("empty-state") { +"Заказов пока нет." }
        } else {
            table {
                thead {
                    tr {
                        th { +"ID" }; th { +"Описание" }; th { +"Сумма" }; th { +"Статус" }; th { }
                    }
                }
                tbody {
                    orders.forEach { order ->
                        tr {
                            td { +order.id.asString.take(8) }
                            td { +order.description }
                            td { +order.amount.toPlainString() }
                            td { +order.status.name }
                            td {
                                a(href = "/orders/${order.id.asString}?transport=$transport",
                                    classes = "btn btn-primary") { +"Открыть" }
                            }
                        }
                    }
                }
            }
        }
    }
}
