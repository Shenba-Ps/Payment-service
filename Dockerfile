# Use Maven + JDK image to build the jar
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build jar (skip tests for faster build)
RUN mvn clean package -DskipTests

# Second stage: runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 9090

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
