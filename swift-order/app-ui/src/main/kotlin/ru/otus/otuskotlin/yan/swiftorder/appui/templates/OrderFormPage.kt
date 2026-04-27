package ru.otus.otuskotlin.yan.swiftorder.appui.templates

import kotlinx.html.*
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus

fun FlowContent.orderFormPage(order: SwiftOrder?, transport: String) {
    val isEdit = order != null && order.id.asString.isNotBlank()
    val backHref = if (isEdit) "/orders/${order!!.id.asString}?transport=$transport"
                  else "/orders?transport=$transport"
    val action   = if (isEdit) "/orders/${order!!.id.asString}?transport=$transport"
                  else "/orders?transport=$transport"

    a(href = backHref, classes = "back-link") { +"← Назад" }
    div("card") {
        h1 { +(if (isEdit) "Редактировать заказ" else "Новый заказ") }
        form(action = action, method = FormMethod.post) {
            if (isEdit) {
                hiddenInput { name = "id"; value = order!!.id.asString }
            }

            label { +"Описание" }
            textInput { name = "description"; value = order?.description ?: ""; placeholder = "Описание заказа" }

            label { +"Сумма" }
            numberInput { name = "amount"; value = order?.amount?.toPlainString() ?: ""; placeholder = "0" }

            label { +"Владелец ID" }
            textInput { name = "ownerId"; value = order?.ownerId?.asString ?: ""; placeholder = "UUID владельца" }

            label { +"Файл ID" }
            textInput { name = "fileId"; value = order?.fileId?.asString ?: ""; placeholder = "имя файла" }

            if (isEdit) {
                label { +"Статус" }
                select {
                    name = "status"
                    SwiftOrderStatus.values().forEach { s ->
                        option {
                            value = s.name
                            selected = (s == order?.status)
                            +s.name
                        }
                    }
                }
            }

            div("actions") {
                submitInput(classes = "btn btn-primary") { value = "Сохранить" }
                a(href = backHref, classes = "btn btn-secondary") { +"Отмена" }
            }
        }
    }
}
