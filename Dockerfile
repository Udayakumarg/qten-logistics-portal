# ======================================
# QTEN Logistics Portal - Spring Boot (Java 17)
# ======================================

# 1️⃣ Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source and build
COPY src ./src
RUN mvn clean package -DskipTests

# 2️⃣ Run Stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Render’s dynamic port
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
