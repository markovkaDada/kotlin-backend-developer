package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.repotests.BaseInitOrders
import ru.otus.otuskotlin.yan.swiftorder.repotests.RepoOrderReadTest

class PostgresOrderReadTest : RepoOrderReadTest() {
    override val repo: ISwiftOrderRepo = SwiftOrderRepoPostgres(
        properties = TestPostgresContainer.properties,
    ).also { it.clearAndSeed(BaseInitOrders.all) }
}
