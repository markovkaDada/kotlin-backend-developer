package ru.otus.otuskotlin.yan.swiftorder.biz.general

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId

fun ICorChainDsl<Context>.trimId() =
    worker("Очистка id") { orderValidating = orderValidating.copy(id = SwiftOrderId(orderValidating.id.asString.trim())) }
