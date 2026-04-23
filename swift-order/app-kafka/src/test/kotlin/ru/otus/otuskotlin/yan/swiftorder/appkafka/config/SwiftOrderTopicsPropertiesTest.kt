package ru.otus.otuskotlin.yan.swiftorder.appkafka.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class SwiftOrderTopicsPropertiesTest {

    private val props = SwiftOrderTopicsProperties()

    @Test
    fun `default request topic names are correct`() {
        assertEquals("swift-order-create-request", props.createRequestTopic.name)
        assertEquals("swift-order-read-request", props.readRequestTopic.name)
        assertEquals("swift-order-update-request", props.updateRequestTopic.name)
        assertEquals("swift-order-delete-request", props.deleteRequestTopic.name)
        assertEquals("swift-order-search-request", props.searchRequestTopic.name)
    }

    @Test
    fun `default response topic names are correct`() {
        assertEquals("swift-order-create-response", props.createResponseTopic.name)
        assertEquals("swift-order-read-response", props.readResponseTopic.name)
        assertEquals("swift-order-update-response", props.updateResponseTopic.name)
        assertEquals("swift-order-delete-response", props.deleteResponseTopic.name)
        assertEquals("swift-order-search-response", props.searchResponseTopic.name)
    }

    @Test
    fun `getAllTopics returns all 10 topics`() {
        assertEquals(10, props.getAllTopics().size)
    }

    @Test
    fun `createTopics defaults to false`() {
        assertFalse(props.createTopics)
    }
}
