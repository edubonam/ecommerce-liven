FROM openjdk:17
WORKDIR /
ARG JAR_FILE=build/**/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]