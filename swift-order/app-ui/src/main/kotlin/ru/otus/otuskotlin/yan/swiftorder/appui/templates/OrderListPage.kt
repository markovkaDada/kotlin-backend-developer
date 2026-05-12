package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus

fun FlowContent.orderListPage(
    orders: List<SwiftOrder>,
    transport: String,
    ownerIdFilter: String = "",
    statusFilter: String = "",
) {
    div("card") {
        div("row-actions") {
            h1 { +"Заказы" }
            a(href = "/orders/new?transport=$transport", classes = "btn btn-primary") { +"+ Создать заказ" }
        }

        form(action = "/orders", method = FormMethod.get) {
            hiddenInput { name = "transport"; value = transport }
            div("filter-row") {
                div("filter-field") {
                    label { htmlFor = "ownerId"; +"Владелец" }
                    textInput {
                        id = "ownerId"; name = "ownerId"
                        placeholder = "ID владельца"
                        value = ownerIdFilter
                    }
                }
                div("filter-field") {
                    label { htmlFor = "status"; +"Статус" }
                    select {
                        id = "status"; name = "status"
                        option { value = ""; +("Все") }
                        SwiftOrderStatus.entries
                            .filter { it != SwiftOrderStatus.NONE }
                            .forEach { s ->
                                option {
                                    value = s.name
                                    selected = s.name == statusFilter
                                    +s.name
                                }
                            }
                    }
                }
                div("filter-field filter-actions") {
                    submitInput(classes = "btn btn-secondary") { value = "Фильтр" }
                    if (ownerIdFilter.isNotBlank() || statusFilter.isNotBlank()) {
                        a(href = "/orders?transport=$transport", classes = "btn") { +"Сбросить" }
                    }
                }
            }
        }

        if (orders.isEmpty()) {
            p("empty-state") { +"Заказов пока нет." }
        } else {
            table {
                thead {
                    tr {
                        th { +"ID" }; th { +"Описание" }; th { +"Сумма" }; th { +"Статус" }; th { +"Владелец" }; th { }
                    }
                }
                tbody {
                    orders.forEach { order ->
                        tr {
                            td { +order.id.asString.take(8) }
                            td { +order.description }
                            td { +order.amount.toPlainString() }
                            td { +order.status.name }
                            td { +order.ownerId.asString }
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
