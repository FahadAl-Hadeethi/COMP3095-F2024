# Build stage:
FROM gradle:8-jdk23-alpine AS builder

# Copy all the files to the container
COPY --chown=gradle:gradle . /home/gradle/src

# Change working directory
WORKDIR /home/gradle/src

# Build the project, creating a jar file, without running any tests
RUN gradle build -x test

# Package stage:
FROM openjdk:23-jdk

# Create an /app directory inside the container
RUN mkdir /app

# Copy the created jar file from the build stage to /app directory
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/order-service.jar

# Set environment variables for PostgreSQL
ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=password

# Expose port 8085 of the container
EXPOSE 8085

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
