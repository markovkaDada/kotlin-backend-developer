package ru.otus.otuskotlin.yan.swiftorder.biz

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.ISwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.biz.general.initStatus
import ru.otus.otuskotlin.yan.swiftorder.biz.operations.orderCreate
import ru.otus.otuskotlin.yan.swiftorder.biz.operations.orderDelete
import ru.otus.otuskotlin.yan.swiftorder.biz.operations.orderRead
import ru.otus.otuskotlin.yan.swiftorder.biz.operations.orderSearch
import ru.otus.otuskotlin.yan.swiftorder.biz.operations.orderUpdate
import ru.otus.otuskotlin.yan.swiftorder.cor.rootChain

class SwiftOrderProcessor : ISwiftOrderProcessor {
    override suspend fun exec(ctx: Context) = businessChain.exec(ctx)

    private val businessChain = rootChain {
        initStatus()
        orderCreate()
        orderRead()
        orderUpdate()
        orderDelete()
        orderSearch()
    }.build()
}
