# Problem: Using Maven image (large) for final runtime
FROM maven:3.9-eclipse-temurin-17

# Problem: Copying everything first (breaks layer caching)
COPY . /app
WORKDIR /app

# Problem: Building inside final image
RUN mvn clean package -DskipTests

# Problem: Running as root, large image size
EXPOSE 8080
CMD ["java", "-jar", "target/order-service-1.0.0.jar"]