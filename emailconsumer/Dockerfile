
# Start with an official Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the entire project into the container
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Use an official Eclipse Temurin image for Java 21 to run the app
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port on which the app runs
EXPOSE 8080

# Define the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]