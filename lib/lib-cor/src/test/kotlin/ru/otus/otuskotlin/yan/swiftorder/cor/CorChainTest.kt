package ru.otus.otuskotlin.yan.swiftorder.cor

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CorChainTest {

    data class TestCtx(var log: MutableList<String> = mutableListOf())

    @Test
    fun `chain executes workers sequentially`() = runBlocking {
        val chain = rootChain<TestCtx> {
            worker("first") { log.add("1") }
            worker("second") { log.add("2") }
            worker("third") { log.add("3") }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(listOf("1", "2", "3"), ctx.log)
    }

    @Test
    fun `chain skips all workers when on returns false`() = runBlocking {
        val chain = rootChain<TestCtx> {
            chain {
                on { false }
                worker("never") { log.add("X") }
            }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(emptyList<String>(), ctx.log)
    }

    @Test
    fun `nested chains execute in order`() = runBlocking {
        val chain = rootChain<TestCtx> {
            chain {
                title = "outer-a"
                worker("a1") { log.add("a1") }
                worker("a2") { log.add("a2") }
            }
            chain {
                title = "outer-b"
                worker("b1") { log.add("b1") }
            }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(listOf("a1", "a2", "b1"), ctx.log)
    }

    @Test
    fun `worker in chain with on condition skips when false`() = runBlocking {
        val chain = rootChain<TestCtx> {
            worker("always") { log.add("always") }
            worker {
                title = "conditional"
                on { log.size > 5 }
                handle { log.add("never") }
            }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(listOf("always"), ctx.log)
    }
}
