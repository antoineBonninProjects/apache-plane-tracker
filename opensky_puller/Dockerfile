# Use a base image with Java 11
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container (replace with your actual jar file name)
COPY target/opensky_puller-1.0.jar /app/opensky_puller.jar

# Set the entrypoint to run the Java application
ENTRYPOINT ["java", "-jar", "/app/opensky_puller.jar"]
