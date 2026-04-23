package ru.otus.otuskotlin.yan.swiftorder.appkafka.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TopicTest {

    @Test
    fun `default values are set correctly`() {
        val topic = Topic()
        assertEquals("", topic.name)
        assertEquals(1, topic.partitions)
        assertEquals(1, topic.replicationFactor)
        assertEquals(1, topic.concurrency)
        assertEquals(5000, topic.pollTimeout)
    }

    @Test
    fun `custom values are retained`() {
        val topic = Topic(name = "my-topic", partitions = 3, replicationFactor = 2, concurrency = 4, pollTimeout = 10000)
        assertEquals("my-topic", topic.name)
        assertEquals(3, topic.partitions)
        assertEquals(2.toShort(), topic.replicationFactor)
        assertEquals(4, topic.concurrency)
        assertEquals(10000, topic.pollTimeout)
    }
}
