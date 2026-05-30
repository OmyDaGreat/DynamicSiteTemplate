package xyz.malefic.dynamicsite.server

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.filter.AllowAllOriginPolicy
import org.http4k.filter.CorsPolicy
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.nio.file.Files
import java.nio.file.Paths

val corsPolicy = CorsPolicy(
    headers = listOf("Content-Type"),
    methods = listOf(GET, POST, PUT, DELETE),
    originPolicy = AllowAllOriginPolicy,
)

private fun serveStaticFile(req: Request): Response {
    val requestPath = req.uri.path.removePrefix("/")
    
    // When running jvmRun, the CWD is the site directory
    // Built frontend is at build/dist/js/productionExecutable/public/
    val baseDir = Paths.get("build", "dist", "js", "productionExecutable", "public").toAbsolutePath()
    val fileName = if (requestPath.isEmpty()) "index.html" else requestPath
    val filePath = baseDir.resolve(fileName).normalize()
    
    return if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
        try {
            val content = Files.readAllBytes(filePath)
            Response(OK).body(String(content))
        } catch (e: Exception) {
            Response(NOT_FOUND)
        }
    } else {
        Response(NOT_FOUND)
    }
}

val apiRoutes: RoutingHttpHandler = routes(
    "/api/ping" bind GET to { Response(OK).body("pong") },
    "/api/health" bind GET to { Response(OK).body("healthy") },
)

val http: HttpHandler = object : HttpHandler {
    override fun invoke(req: Request): Response {
        return try {
            apiRoutes(req)
        } catch (e: Exception) {
            serveStaticFile(req)
        }
    }
}



