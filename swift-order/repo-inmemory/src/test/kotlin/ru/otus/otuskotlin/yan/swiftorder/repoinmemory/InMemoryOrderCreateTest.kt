package ru.otus.otuskotlin.yan.swiftorder.repoinmemory

import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import ru.otus.otuskotlin.yan.swiftorder.repotests.RepoOrderCreateTest

class InMemoryOrderCreateTest : RepoOrderCreateTest() {
    override val repo: ISwiftOrderRepo = SwiftOrderRepoInMemory(initOrders = BaseInitOrders.all)
}
