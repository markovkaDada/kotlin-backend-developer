package ru.otus.otuskotlin.yan.swiftorder.appspring

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.yan.swiftorder.appspring.config.AppConfig
import ru.otus.otuskotlin.yan.swiftorder.appspring.config.RepoConfig

class AppConfigTest {

    @Test
    fun `appSettings bean is created with processor and loggerProvider`() {
        val appSettings = AppConfig(RepoConfig()).appSettings()
        assertNotNull(appSettings)
        assertNotNull(appSettings.processor)
        assertNotNull(appSettings.loggerProvider)
    }
}
