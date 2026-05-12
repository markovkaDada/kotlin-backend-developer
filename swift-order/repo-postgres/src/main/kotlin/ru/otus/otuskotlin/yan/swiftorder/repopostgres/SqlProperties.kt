package ru.otus.otuskotlin.yan.swiftorder.repopostgres

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "swift-order-pass",
    val database: String = "swift_order",
) {
    val url: String get() = "jdbc:postgresql://$host:$port/$database"
}
