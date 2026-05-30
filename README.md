# Dynamic Site Template

A unified Kotlin Multiplatform (KMP) project combining a Kobweb frontend with an http4k backend server.

## Architecture

This is a **single-module KMP project** with two targets:

### jsMain - Kobweb Frontend
- **Location**: `site/src/jsMain`
- Compose-based web UI built with Kobweb
- Compiles to JavaScript/WebAssembly
- Built artifacts: `site/build/dist/js/productionExecutable/public/`

### jvmMain - http4k Backend
- **Location**: `site/src/jvmMain`
- HTTP server built with http4k framework
- Runs on Undertow server backend (port 8080)
- Serves API endpoints and static frontend files

## Building

Build all targets:
```bash
./gradlew build
```

## Running

### Start the Server
Runs the jvmMain entry point:
```bash
./gradlew jvmRun
```

The server will start on `http://localhost:8080`

## API Endpoints

- **`GET /api/ping`** - Returns "pong"
- **`GET /api/health`** - Returns "healthy"
- **`GET /`** or **`GET /index.html`** - Serves the frontend (after it's built)

## CORS

All endpoints support CORS with the following headers:
- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Headers: Content-Type`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE`

## Project Structure

```
site/
├── src/
│   ├── jsMain/          # Kobweb frontend (compiles to JS)
│   │   └── kotlin/
│   └── jvmMain/         # http4k server (compiles to JVM bytecode)
│       └── kotlin/xyz/malefic/dynamicsite/server/
│           ├── Main.kt  # Entry point
│           └── Http.kt  # Routes & handlers
├── build.gradle.kts     # KMP configuration
└── build/
    └── dist/js/productionExecutable/public/  # Built frontend files
```

## Development

The advantages of this unified KMP structure:

1. **Single build system** - One Gradle configuration for both targets
2. **Shared dependencies** - Common code can be placed in commonMain (if needed)
3. **Type-safe APIs** - Frontend and backend can share Kotlin data classes
4. **Simplified deployment** - Single module to version control and distribute
5. **Clean separation** - Platform-specific code stays in jsMain/jvmMain respectively
