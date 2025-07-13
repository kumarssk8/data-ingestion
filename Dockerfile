FROM amazoncorretto:21-alpine

WORKDIR /app

COPY build/libs/ingestion-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
