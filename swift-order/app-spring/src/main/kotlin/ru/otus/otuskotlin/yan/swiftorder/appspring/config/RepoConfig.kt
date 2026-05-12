package ru.otus.otuskotlin.yan.swiftorder.appspring.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "swift-order.repo")
data class RepoConfig(
    val type: RepoType = RepoType.INMEMORY,
    val config: RepoSpecificConfig = RepoSpecificConfig(),
) {
    enum class RepoType { INMEMORY, POSTGRES }

    data class RepoSpecificConfig(
        val maxEntries: Int = 1000,
        val host: String = "localhost",
        val port: Int = 5432,
        val user: String = "postgres",
        val password: String = "swift-order-pass",
        val database: String = "swift_order",
    )
}
