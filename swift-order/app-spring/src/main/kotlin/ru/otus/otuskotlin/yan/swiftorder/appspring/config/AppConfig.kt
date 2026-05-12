package ru.otus.otuskotlin.yan.swiftorder.appspring.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.appcommon.repo.ISwiftOrderRepo
import ru.otus.otuskotlin.yan.swiftorder.biz.SwiftOrderProcessor
import ru.otus.otuskotlin.yan.swiftorder.repoinmemory.SwiftOrderRepoInMemory
import ru.otus.otuskotlin.yan.swiftorder.repopostgres.SqlProperties
import ru.otus.otuskotlin.yan.swiftorder.repopostgres.SwiftOrderRepoPostgres

@Configuration
@EnableConfigurationProperties(RepoConfig::class)
class AppConfig(private val repoConfig: RepoConfig) {

    @Bean
    fun appSettings(): AppSettings {
        val repo: ISwiftOrderRepo = when (repoConfig.type) {
            RepoConfig.RepoType.INMEMORY -> SwiftOrderRepoInMemory(
                maxEntries = repoConfig.config.maxEntries,
            )
            RepoConfig.RepoType.POSTGRES -> SwiftOrderRepoPostgres(
                properties = SqlProperties(
                    host = repoConfig.config.host,
                    port = repoConfig.config.port,
                    user = repoConfig.config.user,
                    password = repoConfig.config.password,
                    database = repoConfig.config.database,
                )
            )
        }
        return AppSettings(processor = SwiftOrderProcessor(repo = repo))
    }

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
}
