FROM eclipse-temurin:21-jdk-alpine
COPY build/libs/guardpay-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]