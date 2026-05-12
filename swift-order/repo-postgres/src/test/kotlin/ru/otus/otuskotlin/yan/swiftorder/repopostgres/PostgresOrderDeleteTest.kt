package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import ru.otus.otuskotlin.yan.swiftorder.repotests.RepoOrderDeleteTest

class PostgresOrderDeleteTest : RepoOrderDeleteTest() {
    override val repo: ISwiftOrderRepo = SwiftOrderRepoPostgres(
        properties = TestPostgresContainer.properties,
    ).also { it.clearAndSeed(BaseInitOrders.all) }
}
