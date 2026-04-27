package ru.otus.otuskotlin.yan.swiftorder.mappers

import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftFileId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrder
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderId
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOrderStatus
import ru.otus.otuskotlin.yan.swiftorder.models.SwiftOwnerId
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OrderMapperTest {

    @Test
    fun `OrderCreateRequest toInternal maps all fields`() {
        val request = OrderCreateRequest(
            order = OrderCreateObject(
                description = "Cut steel 5mm",
                amount = BigDecimal(1500.0),
                ownerId = "owner-1",
                fileId = "detail.dxf",
            ),
        )
        val result = request.toInternal()
        assertNotNull(result.id)
        assertEquals("Cut steel 5mm", result.description)
        assertEquals(BigDecimal(1500.0), result.amount)
        assertEquals(SwiftOrderStatus.NEW, result.status)
        assertEquals("owner-1", result.ownerId.asString)
        assertEquals("detail.dxf", result.fileId.asString)
    }

    @Test
    fun `OrderUpdateRequest toInternal maps all fields`() {
        val request = OrderUpdateRequest(
            order = OrderUpdateObject(
                id = "order-1",
                description = "Updated description",
                amount = BigDecimal(100.0),
                status = OrderStatusDto.CONFIRMED,
                ownerId = "owner-1",
                fileId = "detail.dxf",
            ),
        )
        val result = request.toInternal()
        assertEquals("order-1", result.id.asString)
        assertEquals("Updated description", result.description)
        assertEquals(BigDecimal(100.0), result.amount)
        assertEquals(SwiftOrderStatus.CONFIRMED, result.status)
        assertEquals("owner-1", result.ownerId.asString)
        assertEquals("detail.dxf", result.fileId.asString)
    }

    @Test
    fun `OrderStatusDto toInternal maps all values`() {
        assertEquals(SwiftOrderStatus.NEW, OrderStatusDto.NEW.toInternal())
        assertEquals(SwiftOrderStatus.CONFIRMED, OrderStatusDto.CONFIRMED.toInternal())
        assertEquals(SwiftOrderStatus.IN_PROGRESS, OrderStatusDto.IN_PROGRESS.toInternal())
        assertEquals(SwiftOrderStatus.COMPLETED, OrderStatusDto.COMPLETED.toInternal())
        assertEquals(SwiftOrderStatus.CANCELLED, OrderStatusDto.CANCELLED.toInternal())
    }

    @Test
    fun `SwiftOrderStatus toTransport maps all values`() {
        assertEquals(OrderStatusDto.NEW, SwiftOrderStatus.NEW.toTransport())
        assertEquals(OrderStatusDto.CONFIRMED, SwiftOrderStatus.CONFIRMED.toTransport())
        assertEquals(OrderStatusDto.IN_PROGRESS, SwiftOrderStatus.IN_PROGRESS.toTransport())
        assertEquals(OrderStatusDto.COMPLETED, SwiftOrderStatus.COMPLETED.toTransport())
        assertEquals(OrderStatusDto.CANCELLED, SwiftOrderStatus.CANCELLED.toTransport())
    }

    @Test
    fun `SwiftOrder toResponseObject maps all fields`() {
        val order = SwiftOrder(
            id = SwiftOrderId("order-1"),
            description = "Cut steel 5mm",
            amount = BigDecimal(100.0),
            status = SwiftOrderStatus.NEW,
            ownerId = SwiftOwnerId("owner-1"),
            fileId = SwiftFileId("detail.dxf"),
        )
        val obj = order.toResponseObject()
        assertEquals("order-1", obj.id)
        assertEquals("Cut steel 5mm", obj.description)
        assertEquals(BigDecimal(100.0), obj.amount)
        assertEquals(OrderStatusDto.NEW, obj.status)
        assertEquals("owner-1", obj.ownerId)
        assertEquals("detail.dxf", obj.fileId)
    }

    @Test
    fun `SwiftOrder toResponseObject maps status COMPLETED`() {
        val order = SwiftOrder(
            id = SwiftOrderId("order-2"),
            description = "Cut aluminium 3mm",
            amount = BigDecimal(100.0),
            status = SwiftOrderStatus.COMPLETED,
            ownerId = SwiftOwnerId("owner-2"),
            fileId = SwiftFileId("part.dxf"),
        )
        val obj = order.toResponseObject()
        assertEquals("order-2", obj.id)
        assertEquals("Cut aluminium 3mm", obj.description)
        assertEquals(OrderStatusDto.COMPLETED, obj.status)
    }

    @Test
    fun `SwiftOrder toResponseObject maps status IN_PROGRESS`() {
        val order = SwiftOrder(
            id = SwiftOrderId("order-3"),
            description = "Cut copper 2mm",
            amount = BigDecimal(100.0),
            status = SwiftOrderStatus.IN_PROGRESS,
            ownerId = SwiftOwnerId("order-3"),
            fileId = SwiftFileId("copper.dxf"),
        )
        val obj = order.toResponseObject()
        assertEquals("order-3", obj.id)
        assertEquals("Cut copper 2mm", obj.description)
        assertEquals(OrderStatusDto.IN_PROGRESS, obj.status)
    }

    @Test
    fun `SwiftOrder toResponseObject maps id for delete`() {
        val order = SwiftOrder(
            id = SwiftOrderId("order-4"),
            description = "desc",
            amount = BigDecimal(100.0),
            status = SwiftOrderStatus.NEW,
            ownerId = SwiftOwnerId("owner-1"),
            fileId = SwiftFileId("file.dxf"),
        )
        assertEquals("order-4", order.toResponseObject().id)
    }

    @Test
    fun `List of SwiftOrder toResponseObject maps all orders`() {
        val orders = listOf(
            SwiftOrder(id = SwiftOrderId("s-1"), description = "Order 1", amount = BigDecimal(100.0), status = SwiftOrderStatus.NEW, ownerId = SwiftOwnerId("o-1"), fileId = SwiftFileId("f-1")),
            SwiftOrder(id = SwiftOrderId("s-2"), description = "Order 2", amount = BigDecimal(200.0), status = SwiftOrderStatus.CONFIRMED, ownerId = SwiftOwnerId("o-2"), fileId = SwiftFileId("f-2")),
        )
        val result = orders.map { it.toResponseObject() }
        assertEquals(2, result.size)
        assertEquals("s-1", result[0].id)
        assertEquals("s-2", result[1].id)
    }
}
