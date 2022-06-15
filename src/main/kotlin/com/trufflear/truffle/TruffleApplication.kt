package com.trufflear.truffle

import io.grpc.Server
import io.grpc.ServerBuilder


class TruffleApplication(private val port: Int) {
    val server: Server = ServerBuilder
            .forPort(port)
            .addService(HelloService())
            .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
                Thread {
                    println("*** shutting down gRPC server since JVM is shutting down")
                    this@TruffleApplication.stop()
                    println("*** server shut down")
                }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = TruffleApplication(port)
    server.start()
    server.blockUntilShutdown()
}
