package com.trufflear.truffle

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.grpc.Server
import io.grpc.ServerBuilder
import javax.sql.DataSource


class TruffleApplication(
    private val port: Int,
    dataSource: DataSource
) {
    val server: Server = ServerBuilder
            .forPort(port)
            .addService(CardTransformationService(dataSource))
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

private fun getHikariDataSource() =
    HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://database-1.cluster-cygt5mfbjjmh.us-west-1.rds.amazonaws.com:3306/TruffleDatabase"
            username = "admin"
            password = "KingOysterBoo123"
            driverClassName = "com.mysql.cj.jdbc.Driver"
        }
    )

fun main() {

    val datasource = getHikariDataSource()
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = TruffleApplication(port, datasource)
    server.start()
    server.blockUntilShutdown()
}
