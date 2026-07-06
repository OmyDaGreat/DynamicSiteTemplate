ARG TARGETARCH

# Build stage
FROM eclipse-temurin:26-jdk AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle.properties ./

RUN chmod +x ./gradlew

COPY . .

RUN ./gradlew :site:dockerRuntime

# Runtime stage
FROM eclipse-temurin:26-jre

WORKDIR /app

COPY --from=builder /app/site/build/docker /app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD wget -qO- http://localhost:8080/api/health || exit 1

ENV PORT=8080

ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:-} -cp /app/lib/*:/app/app.jar xyz.malefic.dynamicsite.server.MainKt"]
