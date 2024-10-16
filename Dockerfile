FROM openjdk:17-jdk-slim

# ARG 지시어로 JAR_FILE을 정의하되, 명시적으로 JAR 파일을 지정합니다.
ARG JAR_FILE=spring-usersetting-0.0.1-SNAPSHOT.jar
ADD build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
