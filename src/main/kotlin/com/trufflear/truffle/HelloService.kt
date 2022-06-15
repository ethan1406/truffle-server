package com.trufflear.truffle

internal class HelloService: HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {

    override suspend fun hello(request: HelloRequest): HelloResponse =
            HelloResponse.newBuilder()
                    .setGreeting("Hello ${request.firstName}")
                    .build()
}