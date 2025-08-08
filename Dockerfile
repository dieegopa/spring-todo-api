# ----------- Stage 1: Build with Gradle -----------
FROM gradle:7.5-jdk17 AS builder

# Working directory
WORKDIR /app

# Copy only configuration files first to leverage cache
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Create Gradle cache directory and set correct permissions
RUN mkdir -p /home/gradle/.gradle && chown -R gradle:gradle /home/gradle

RUN mkdir -p /app && chown -R gradle:gradle /app
RUN mkdir -p /app/gradle/.gradle && chown -R gradle:gradle /home/gradle

# Switch to gradle user (prevents permission issues)
USER gradle

# Download dependencies (this will be cached)
RUN ./gradlew dependencies || return 0

# Copy the rest of the source code
COPY --chown=gradle:gradle . .

# Build the project
RUN ./gradlew build --no-daemon

# ----------- Stage 2: Final image for production -----------
FROM openjdk:17-jdk-slim

# Working directory
WORKDIR /app

# Copy the JAR from the previous stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Execution command
ENTRYPOINT ["java", "-jar", "app.jar"]
