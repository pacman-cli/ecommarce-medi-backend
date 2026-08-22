# syntax=docker/dockerfile:1
# Multi-stage production build for Spring Boot E-Commerce Application

# ==============================================================================
# Stage 1: Build stage with Maven and Eclipse Temurin JDK 17
# ==============================================================================
FROM maven:3-eclipse-temurin-24 AS builder
WORKDIR /app

ENV MAVEN_OPTS="-Xmx384m -XX:MaxMetaspaceSize=256m -Dfile.encoding=UTF-8"

COPY pom.xml .
RUN mvn -B dependency:go-offline -DskipTests

COPY src ./src
RUN mvn -B clean package -DskipTests

# ==============================================================================
# Stage 2: Minimal runtime image with Eclipse Temurin JRE 17 Alpine
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS runner

RUN apk add --no-cache wget curl tzdata

ENV TZ=UTC

RUN addgroup -S ecommerce -g 10001 && \
    adduser -S ecommerce -G ecommerce -u 10001

WORKDIR /app

COPY --from=builder /app/target/ecommerce-backend-*.jar app.jar

RUN chown -R ecommerce:ecommerce /app

USER ecommerce

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:${SERVER_PORT:-8080}/actuator/health || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]
