package ru.otus.otuskotlin.yan.swiftorder.appspring

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.IResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderDeleteResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderSearchResponse
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.yan.swiftorder.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.yan.swiftorder.appcommon.AppSettings
import ru.otus.otuskotlin.yan.swiftorder.mappers.controllerHelper
import ru.otus.otuskotlin.yan.swiftorder.mappers.fromTransport
import ru.otus.otuskotlin.yan.swiftorder.mappers.toTransport
import kotlin.reflect.KClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RestController
@RequestMapping("/v1/order")
class OrderController(
    private val appSettings: AppSettings
) {

    @PostMapping("/create")
    suspend fun create(@RequestBody request: OrderCreateRequest) =
        withContext(Dispatchers.Default) {
            process<OrderCreateRequest, OrderCreateResponse>(appSettings, request, OrderController::class, "create")
        }

    @PostMapping("/read")
    suspend fun read(@RequestBody request: OrderReadRequest) =
        withContext(Dispatchers.Default) {
            process<OrderReadRequest, OrderReadResponse>(appSettings, request, OrderController::class, "read")
        }

    @PostMapping("/update")
    suspend fun update(@RequestBody request: OrderUpdateRequest) =
        withContext(Dispatchers.Default) {
            process<OrderUpdateRequest, OrderUpdateResponse>(appSettings, request, OrderController::class, "update")
        }

    @PostMapping("/delete")
    suspend fun delete(@RequestBody request: OrderDeleteRequest) =
        withContext(Dispatchers.Default) {
            process<OrderDeleteRequest, OrderDeleteResponse>(appSettings, request, OrderController::class, "delete")
        }

    @PostMapping("/search")
    suspend fun search(@RequestBody request: OrderSearchRequest) =
        withContext(Dispatchers.Default) {
            process<OrderSearchRequest, OrderSearchResponse>(appSettings, request, OrderController::class, "search")
        }

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: AppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransport() as R },
            clazz,
            logId,
        )
    }
}
