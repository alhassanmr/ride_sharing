# Use a base image with OpenJDK 17 installed
FROM openjdk:17-jdk-alpine3.14

# Set the working directory to /app
WORKDIR /app

# Copy the project files into the image
COPY target/*.jar app.jar

# Set the entrypoint command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]