package ru.otus.otuskotlin.yan.swiftorder.repopostgres

import org.testcontainers.containers.PostgreSQLContainer

object TestPostgresContainer {
    private val container: PostgreSQLContainer<Nothing> = PostgreSQLContainer("postgres:17")

    val properties: SqlProperties by lazy {
        container.start()
        SqlProperties(
            host = container.host,
            port = container.firstMappedPort,
            user = container.username,
            password = container.password,
            database = container.databaseName,
        )
    }
}
