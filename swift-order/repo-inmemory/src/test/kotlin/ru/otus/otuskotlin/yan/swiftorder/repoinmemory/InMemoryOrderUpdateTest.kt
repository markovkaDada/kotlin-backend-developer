package ru.otus.otuskotlin.yan.swiftorder.repoinmemory

import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import ru.otus.otuskotlin.yan.swiftorder.repotests.RepoOrderUpdateTest

class InMemoryOrderUpdateTest : RepoOrderUpdateTest() {
    override val repo: ISwiftOrderRepo = SwiftOrderRepoInMemory(initOrders = BaseInitOrders.all)
}
