ARG TARGETARCH

# Multi-stage build
FROM eclipse-temurin:23-jdk AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle.properties ./

RUN chmod +x ./gradlew

COPY . .

# Build both jsMain and jvmMain
RUN ./gradlew :site:jvmJar :site:compileProductionExecutableKotlinJs

# Runtime stage
FROM eclipse-temurin:26-jre-alpine

WORKDIR /app

# Copy compiled sources
COPY --from=builder /app/site/build /app/site/build
COPY --from=builder /app/.gradle /app/.gradle
COPY --from=builder /app/gradle /app/gradle
COPY --from=builder /app/gradlew /app/gradlew
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/

RUN chmod +x /app/gradlew

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD wget -qO- http://localhost:8000/api/health || exit 1

WORKDIR /app/site

ENTRYPOINT ["/app/gradlew", "jvmRun"]
