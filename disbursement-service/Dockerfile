# # Use a base image with JDK 21
# FROM openjdk:21-jdk-slim

# # Set the working directory
# WORKDIR /app

# # Copy the Maven wrapper and the pom.xml file
# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .

# # Download the dependencies
# RUN ./mvnw dependency:go-offline

# # Copy the source code
# COPY src ./src

# # Build the application
# RUN ./mvnw package -DskipTests

# # Expose the port the application runs on
# EXPOSE 8083

# # Run the application
# CMD ["java", "-jar", "target/disbursement-0.0.1-SNAPSHOT.jar"]


FROM openjdk:21-jdk-slim AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]