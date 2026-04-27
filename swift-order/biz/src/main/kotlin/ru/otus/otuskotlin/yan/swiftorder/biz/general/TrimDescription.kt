package ru.otus.otuskotlin.yan.swiftorder.biz.general

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.worker

fun ICorChainDsl<Context>.trimDescription() =
    worker("Очистка description") { orderValidating = orderValidating.copy(description = orderValidating.description.trim()) }
