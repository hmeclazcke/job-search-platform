FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/internal-jobs-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]