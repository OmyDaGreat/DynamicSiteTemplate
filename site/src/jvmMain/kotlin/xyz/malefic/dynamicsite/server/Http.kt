package xyz.malefic.dynamicsite.server

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.HttpHandler
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.AllowAllOriginPolicy
import org.http4k.filter.CorsPolicy
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import xyz.malefic.dynamicsite.model.Message
import java.nio.file.Files
import java.nio.file.Paths

val corsPolicy =
    CorsPolicy(
        headers = listOf("Content-Type"),
        methods = listOf(GET, POST, PUT, DELETE),
        originPolicy = AllowAllOriginPolicy,
    )

private val json = Json { ignoreUnknownKeys = true }

private fun serveStaticFile(req: Request): Response {
    val requestPath = req.uri.path.removePrefix("/")

    // Support both development and Docker deployments:
    // - Development: site/build/dist/js/productionExecutable/public/ (relative to site/)
    // - Docker: /app/site/build/dist/js/productionExecutable/public/ (absolute path in container)
    val staticDirs = listOf(
        Paths.get("build", "dist", "js", "productionExecutable"),       // generated JS bundle
        Paths.get("build", "dist", "js", "productionExecutable", "public"),  // dev from site/
        Paths.get("/app", "site", "build", "dist", "js", "productionExecutable"),
        Paths.get("/app", "site", "build", "dist", "js", "productionExecutable", "public"),
    )

    val fileName = requestPath.ifEmpty { "index.html" }

    for (baseDir in staticDirs) {
        val filePath = baseDir.resolve(fileName).normalize()
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            return try {
                val content = Files.readAllBytes(filePath)
                Response(OK).body(String(content))
            } catch (e: Exception) {
                Response(NOT_FOUND).body(e.toString())
            }
        }
    }

    return Response(NOT_FOUND)
}

val apiRoutes: RoutingHttpHandler =
    routes(
        "/api/ping" bind GET to { Response(OK).body("pong") },
        "/api/health" bind GET to { Response(OK).body("healthy") },
        "/api/messages" bind GET to {
            val messages = listOf(
                Message(1, "Hello from the server!", System.currentTimeMillis()),
                Message(2, "This data is shared via commonMain!", System.currentTimeMillis() - 5000),
                Message(3, "Fetched via JSON API", System.currentTimeMillis() - 10000),
            )
            Response(OK)
                .header("Content-Type", APPLICATION_JSON.value)
                .body(json.encodeToString(messages))
        },
    )

val http: HttpHandler =
    object : HttpHandler {
        override fun invoke(req: Request): Response =
            try {
                val response = apiRoutes(req)
                if (response.status == NOT_FOUND) serveStaticFile(req) else response
            } catch (e: Exception) {
                serveStaticFile(req)
            }
    }
