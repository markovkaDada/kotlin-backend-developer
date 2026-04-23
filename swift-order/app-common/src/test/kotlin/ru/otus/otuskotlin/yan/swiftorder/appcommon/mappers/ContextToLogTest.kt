package ru.otus.otuskotlin.yan.swiftorder.appcommon.mappers

import ru.otus.otuskotlin.yan.swiftorder.appcommon.Context
import ru.otus.otuskotlin.yan.swiftorder.appcommon.error.SOError
import ru.otus.otuskotlin.yan.swiftorder.models.Command
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ContextToLogTest {

    @Test
    fun `toLog returns correct logId and source`() {
        val log = Context().toLog("req-42")
        assertEquals("req-42", log.logId)
        assertEquals("swift-order", log.source)
    }

    @Test
    fun `toLog with no errors returns empty errors list`() {
        val log = Context().toLog("req")
        assertTrue(log.errors.isEmpty())
    }

    @Test
    fun `toLog with error includes error in log`() {
        val ctx = Context(
            command = Command.CREATE,
            errors = mutableListOf(SOError(code = "err-1", group = "test", message = "something failed")),
        )
        val log = ctx.toLog("req")
        assertEquals(1, log.errors.size)
        assertEquals("err-1", log.errors[0].code)
    }

    @Test
    fun `toLog with NONE command has null order log`() {
        val log = Context().toLog("req")
        assertNull(log.order)
    }

    @Test
    fun `toLog with CREATE command includes operation in order log`() {
        val ctx = Context(command = Command.CREATE)
        val log = ctx.toLog("req")
        assertEquals("swift-order", log.source)
    }
}
