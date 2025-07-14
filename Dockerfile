FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/ingestion-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx2048m -Dspring.profiles.active=prod"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
