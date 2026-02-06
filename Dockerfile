# Use Maven + JDK 17 image
FROM maven:3.9.9-eclipse-temurin-17

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first (enables Docker layer caching)
COPY pom.xml .

# Download dependencies
RUN mvn -q -e -DskipTests dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Default command (can be overridden)
CMD ["mvn", "test"]
