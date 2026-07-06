package xyz.malefic.dynamicsite.server

import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.filter.debug
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    val server =
        ServerFilters
            .Cors(corsPolicy)
            .then(http)
            .debug()
            .asServer(Undertow(port))
            .start()

    println("Server started on port ${server.port()}!")
}
