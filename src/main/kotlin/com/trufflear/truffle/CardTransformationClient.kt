package com.trufflear.truffle

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.Closeable
import java.util.concurrent.TimeUnit

class CardTransformationClient(private val channel: ManagedChannel) : Closeable {
    private val stub: CardTransformationGrpcKt.CardTransformationCoroutineStub =  CardTransformationGrpcKt.CardTransformationCoroutineStub(channel)

    suspend fun getCard() {
        val request = getCardTransformationDataRequest {
            platform = "android"
        }

        val response = stub.getCardTransformationData(request)

        println(response)
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toInt() ?: 50051

    val channel = ManagedChannelBuilder
            .forAddress("localhost", port)
            .usePlaintext()
            .executor(Dispatchers.IO.asExecutor())
            .build()

    val client = CardTransformationClient(channel)

    client.getCard()
}