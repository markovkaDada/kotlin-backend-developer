package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import ru.otus.otuskotlin.yan.swiftorder.repotests.RepoOrderUpdateTest

class PostgresOrderUpdateTest : RepoOrderUpdateTest() {
    override val repo: ISwiftOrderRepo = SwiftOrderRepoPostgres(
        properties = TestPostgresContainer.properties,
    ).also { it.clearAndSeed(BaseInitOrders.all) }
}
