package ru.otus.otuskotlin.yan.swiftorder.appspring

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.otus.otuskotlin.yan.swiftorder.appspring.config.AppConfig

class AppConfigTest {

    @Test
    fun `appSettings bean is created with processor and loggerProvider`() {
        val appSettings = AppConfig().appSettings()
        assertNotNull(appSettings)
        assertNotNull(appSettings.processor)
        assertNotNull(appSettings.loggerProvider)
    }
}
