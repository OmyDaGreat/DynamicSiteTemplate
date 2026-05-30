# Docker Setup for DynamicSiteTemplate

This project has been adapted with Docker support, based on patterns from KanManServer but tailored for a Kotlin Multiplatform (KMP) application with both Kobweb frontend and http4k backend.

## Files Added

- **Dockerfile** - Multi-stage build: compiles both jsMain and jvmMain, then packages into minimal JRE runtime
- **docker-compose.yml** - Orchestrates the application with healthcheck and resource limits
- **.dockerignore** - Optimizes Docker build context for faster builds
- **.github/workflows/docker-build-publish.yaml** - CI/CD pipeline that builds and publishes to Docker Hub on push to main

## Quick Start

### Local Development
```bash
# Build the application locally
./gradlew build

# Build Docker image
docker build -t dynamicsite:latest .

# Run with docker-compose
docker compose up
```

### Docker Compose
```bash
# Start
docker compose up

# View logs
docker compose logs -f app

# Stop
docker compose down
```

## How It Works

### Dockerfile Strategy
1. **Builder Stage** (JDK 23): Compiles the entire Kotlin project
   - Builds jsMain (Kobweb frontend with webpack)
   - Builds jvmMain (http4k server code)
   
2. **Runtime Stage** (JRE 26 Alpine): Minimal image with only runtime dependencies
   - Copies compiled JVM classes and resources
   - Copies pre-built static frontend files
   - Runs via `./gradlew jvmRun` (maintains working directory as site/)

### Path Resolution in Http.kt
The Http.kt file supports both deployment modes:
- **Development**: Relative path `build/dist/js/productionExecutable/public/` (from site/ directory)
- **Docker**: Absolute path `/app/static/` (from builder COPY instruction)

Fallback logic tries both paths, allowing flexibility.

## GitHub Actions Workflow

The `docker-build-publish.yaml` workflow:
1. Triggers on push to `main` or pull requests
2. Checks out code
3. Sets up JDK 23
4. Builds with Gradle (skips tests for speed)
5. Sets up Docker Buildx for multi-platform builds
6. Authenticates with Docker Hub (requires `DOCKER_USERNAME` and `DOCKER_PASSWORD` secrets)
7. Builds and pushes images for both `linux/amd64` and `linux/arm64`
8. Tags with git SHA and `latest`

### Required Secrets
Add these to GitHub repository settings:
- `DOCKER_USERNAME` - Docker Hub username
- `DOCKER_PASSWORD` - Docker Hub token or password

### Image Tags
Images are published as:
- `$DOCKER_USERNAME/dynamicsite:$GIT_SHA` - Specific build
- `$DOCKER_USERNAME/dynamicsite:latest` - Latest release

## Health Checks

Both Docker and docker-compose include health checks:
```bash
wget -qO- http://localhost:8000/api/health
```

This endpoint returns "healthy" and confirms the server is running.

## Resource Limits

docker-compose.yml configures:
- Memory limit: 768M
- JAVA_OPTS: `-Xmx512m` (Java heap limit)
- Restart policy: `unless-stopped`

## Differences from KanManServer

| Aspect | KanManServer | DynamicSiteTemplate |
|--------|--------------|---------------------|
| Build type | Standalone JVM app | KMP (JS + JVM) |
| Port | 6320 | 8000 |
| Builder base | JRE only | JDK (needs compilation) |
| Static files | None | Kobweb frontend |
| Path handling | Simple static mount | Dual-path fallback |
| Health check endpoint | `/health` | `/api/health` |

## Troubleshooting

### Build fails with npm issues
The Gradle daemon may run out of metaspace. Increase in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g
```

### Container exits immediately
Check logs:
```bash
docker compose logs app
```

Common causes:
- Port 8000 already in use
- Static files not found (missing `./gradlew build`)

### Health check failing
Verify the server is responding:
```bash
docker compose exec app curl http://localhost:8000/api/health
```

## Next Steps

1. Set up Docker Hub repository and secrets in GitHub
2. Push to main to trigger the workflow
3. Monitor the Actions tab for build status
4. Pull and run images locally: `docker pull $USER/dynamicsite:latest`
