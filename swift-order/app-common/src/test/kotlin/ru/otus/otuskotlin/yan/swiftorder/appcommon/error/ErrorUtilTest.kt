package ru.otus.otuskotlin.yan.swiftorder.appcommon.error

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorUtilTest {

    @Test
    fun `asSOError uses exception message and default codes`() {
        val ex = RuntimeException("test error")
        val error = ex.asSOError()
        assertEquals("unknown", error.code)
        assertEquals("exceptions", error.group)
        assertEquals("test error", error.message)
        assertEquals(ex, error.exception)
    }

    @Test
    fun `asSOError with null message defaults to empty string`() {
        val ex = RuntimeException(null as String?)
        val error = ex.asSOError()
        assertEquals("", error.message)
    }

    @Test
    fun `asSOError custom params override defaults`() {
        val ex = RuntimeException("original")
        val error = ex.asSOError(code = "my-code", group = "my-group", message = "custom msg")
        assertEquals("my-code", error.code)
        assertEquals("my-group", error.group)
        assertEquals("custom msg", error.message)
    }
}
