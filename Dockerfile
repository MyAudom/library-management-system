FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/your-app.jar app.jar
# Adjust if using Gradle: build/libs/your-app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]