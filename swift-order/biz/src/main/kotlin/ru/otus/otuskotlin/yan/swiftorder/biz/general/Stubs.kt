package ru.otus.otuskotlin.yan.swiftorder.biz.general

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.chain
import ru.otus.otuskotlin.yan.swiftorder.models.WorkMode

fun ICorChainDsl<Context>.stubs(block: ICorChainDsl<Context>.() -> Unit) = chain {
    on { workMode == WorkMode.STUB }
    block()
}
