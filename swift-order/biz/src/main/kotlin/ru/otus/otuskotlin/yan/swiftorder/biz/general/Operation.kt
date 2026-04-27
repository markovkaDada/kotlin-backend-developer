package ru.otus.otuskotlin.yan.swiftorder.biz.general

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.cor.ICorChainDsl
import ru.otus.otuskotlin.yan.swiftorder.cor.chain
import ru.otus.otuskotlin.yan.swiftorder.models.Command

fun ICorChainDsl<Context>.operation(
    command: Command,
    block: ICorChainDsl<Context>.() -> Unit,
) = chain {
    on { this.command == command }
    block()
}
