FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ARG PROPERTIES_FILE=./application.properties

COPY ${JAR_FILE} app.jar
COPY ${PROPERTIES_FILE} application.properties

ENTRYPOINT ["java","-jar","/app.jar"]