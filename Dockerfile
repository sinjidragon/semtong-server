FROM openjdk:17-jdk-slim

LABEL authors="socury"

ARG JAR_FILE=./build/libs/semtong-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]