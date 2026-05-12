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
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchFilter
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrderControllerRepoTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val prodDebug = OrderDebug(mode = OrderRequestDebugMode.PROD)

    @Test
    fun `POST create in PROD mode stores and returns order with generated id`() {
        val request = OrderCreateRequest(
            requestType = "create",
            debug = prodDebug,
            order = OrderCreateObject(
                description = "Spring integration test order",
                amount = BigDecimal("300.00"),
                ownerId = "owner-spring",
                fileId = "file-spring",
            ),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("success"))
            .andExpect(jsonPath("$.order.id").isNotEmpty)
            .andExpect(jsonPath("$.order.description").value("Spring integration test order"))
    }

    @Test
    fun `POST search in PROD mode returns empty list from fresh repo`() {
        val request = OrderSearchRequest(
            requestType = "search",
            debug = prodDebug,
            orderFilter = OrderSearchFilter(ownerId = "nobody"),
        )
        val mvcResult = mockMvc.perform(
            post("/v1/order/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(request().asyncStarted()).andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("success"))
            .andExpect(jsonPath("$.errors").doesNotExist())
    }
}
