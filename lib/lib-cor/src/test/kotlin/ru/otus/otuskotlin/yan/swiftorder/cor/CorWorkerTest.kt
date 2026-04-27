package ru.otus.otuskotlin.yan.swiftorder.cor

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CorWorkerTest {

    data class TestCtx(var log: MutableList<String> = mutableListOf(), var value: Int = 0)

    @Test
    fun `worker executes handle when on returns true`() = runBlocking {
        val chain = rootChain<TestCtx> {
            worker("increment") { value += 1 }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(1, ctx.value)
    }

    @Test
    fun `worker skips handle when on returns false`() = runBlocking {
        val chain = rootChain<TestCtx> {
            worker {
                title = "conditional"
                on { value > 10 }
                handle { value += 100 }
            }
        }.build()
        val ctx = TestCtx(value = 0)
        chain.exec(ctx)
        assertEquals(0, ctx.value)
    }

    @Test
    fun `worker calls except on exception`() = runBlocking {
        val chain = rootChain<TestCtx> {
            worker {
                title = "throws"
                handle { throw RuntimeException("oops") }
                except { e -> log.add("caught: ${e.message}") }
            }
        }.build()
        val ctx = TestCtx()
        chain.exec(ctx)
        assertEquals(listOf("caught: oops"), ctx.log)
    }

    @Test
    fun `worker has title and description`() = runBlocking {
        val w = rootChain<TestCtx> {
            worker {
                title = "my-worker"
                description = "does stuff"
                handle { }
            }
        }.build()
        val ctx = TestCtx()
        w.exec(ctx)
    }
}
