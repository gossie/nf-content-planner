FROM openjdk:17-jdk-alpine
ARG JAR_FILE

COPY backend/app/target/nf-content-planner-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
