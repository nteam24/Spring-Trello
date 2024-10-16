FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/spring-usersetting-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "app.jar"]