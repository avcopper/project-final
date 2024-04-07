FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY resources ./resources

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
