FROM openjdk:17-jdk-slim

LABEL authors="socury"

ARG JAR_FILE=./build/libs/shemtong-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

WORKDIR /app
COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"