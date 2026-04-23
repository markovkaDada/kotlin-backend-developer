package ru.otus.otuskotlin.yan.swiftorder.appspring

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDebug
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderStatusDto
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateObject
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrderControllerStubTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val stubDebug = OrderDebug(
        mode = OrderRequestDebugMode.STUB,
        stub = OrderRequestDebugStubs.SUCCESS,
    )

    @Test
    fun `POST create returns stub order`() {
        val request = OrderCreateRequest(
            requestType = "create",
            debug = stubDebug,
            order = OrderCreateObject(
                description = "Test order",
                amount = BigDecimal("150.00"),
                ownerId = "owner-1",
                fileId = "file-1",
            ),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.order.id").value("stub-order-1"))
            .andExpect(jsonPath("$.order.status").value("NEW"))
            .andExpect(jsonPath("$.result").value("success"))
    }

    @Test
    fun `POST read returns stub order`() {
        val request = OrderReadRequest(
            requestType = "read",
            debug = stubDebug,
            order = OrderReadObject(id = "any-id"),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.order.id").value("stub-order-1"))
            .andExpect(jsonPath("$.result").value("success"))
    }

    @Test
    fun `POST update returns stub order`() {
        val request = OrderUpdateRequest(
            requestType = "update",
            debug = stubDebug,
            order = OrderUpdateObject(
                id = "stub-order-1",
                description = "Updated",
                amount = BigDecimal("100.00"),
                status = OrderStatusDto.CONFIRMED,
                ownerId = "owner-1",
                fileId = "file-1",
            ),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.order.id").value("stub-order-1"))
            .andExpect(jsonPath("$.result").value("success"))
    }

    @Test
    fun `POST delete returns stub order`() {
        val request = OrderDeleteRequest(
            requestType = "delete",
            debug = stubDebug,
            order = OrderDeleteObject(id = "stub-order-1"),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.order.id").value("stub-order-1"))
            .andExpect(jsonPath("$.result").value("success"))
    }

    @Test
    fun `POST search returns stub orders list`() {
        val request = OrderSearchRequest(
            requestType = "search",
            debug = stubDebug,
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.orders[0].id").value("stub-order-1"))
            .andExpect(jsonPath("$.orders[1].id").value("stub-order-2"))
            .andExpect(jsonPath("$.result").value("success"))
    }
}
