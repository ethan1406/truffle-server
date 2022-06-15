package com.trufflear.truffle

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable
import java.util.concurrent.TimeUnit

class HelloClient(private val channel: ManagedChannel) : Closeable {
    private val stub: HelloServiceGrpcKt.HelloServiceCoroutineStub = HelloServiceGrpcKt.HelloServiceCoroutineStub(channel)

    suspend fun greet(firstName: String, lastName: String) {
        val request = helloRequest {
            this.firstName = firstName
            this.lastName = lastName
        }
        val response = stub.hello(request)
        println("Received: ${response.greeting}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

//suspend fun main(args: Array<String>) {
//    val port = System.getenv("PORT")?.toInt() ?: 50051
//
//    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
//
//    val client = HelloClient(channel)
//
//    val user = args.singleOrNull() ?: "tom"
//    client.greet(user, user)
//}
