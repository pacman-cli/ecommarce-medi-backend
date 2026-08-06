# syntax=docker/dockerfile:1
# Multi-stage production build for Spring Boot E-Commerce Application

# ==============================================================================
# Stage 1: Build stage with Maven and Eclipse Temurin JDK 17
# ==============================================================================
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Dependency layer caching optimization
COPY pom.xml .
RUN mvn -B dependency:go-offline -DskipTests

# Source code build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ==============================================================================
# Stage 2: Minimal runtime image with Eclipse Temurin JRE 17 Alpine
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS runner

# Install wget/curl for container health checks
RUN apk add --no-cache wget curl tzdata

# Set timezone
ENV TZ=UTC

# Create non-root application user and group for security
RUN addgroup -S ecommerce -g 10001 && \
    adduser -S ecommerce -G ecommerce -u 10001

WORKDIR /app

# Copy compiled JAR artifact from builder stage
COPY --from=builder /app/target/ecommerce-backend-*.jar app.jar

# Ensure appropriate ownership
RUN chown -R ecommerce:ecommerce /app

# Switch to non-root user
USER ecommerce

# Expose Spring Boot HTTP port
EXPOSE 8080

# Health check instructions
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Production JVM flags for memory management and performance
ENTRYPOINT ["java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]
