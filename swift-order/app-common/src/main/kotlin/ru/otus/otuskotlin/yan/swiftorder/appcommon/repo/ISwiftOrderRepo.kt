package ru.otus.otuskotlin.yan.swiftorder.appcommon.repo

interface ISwiftOrderRepo {
    suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse
    suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse
    suspend fun updateOrder(rq: DbOrderRequest): DbOrderResponse
    suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse
    suspend fun searchOrders(rq: DbOrderFilterRequest): DbOrdersResponse

    companion object {
        val NONE = object : ISwiftOrderRepo {
            override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse =
                throw NotImplementedError("Must not be used")
            override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse =
                throw NotImplementedError("Must not be used")
            override suspend fun updateOrder(rq: DbOrderRequest): DbOrderResponse =
                throw NotImplementedError("Must not be used")
            override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse =
                throw NotImplementedError("Must not be used")
            override suspend fun searchOrders(rq: DbOrderFilterRequest): DbOrdersResponse =
                throw NotImplementedError("Must not be used")
        }
    }
}
