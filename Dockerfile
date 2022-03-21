FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE

COPY backend/app/target/nf-content-planner-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=docker", "-Dlogging.level.com.github.gossie.nf.planner=DEBUG","-jar","/app.jar"]
