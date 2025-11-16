FROM eclipse-temurin:21-jdk-alpine
LABEL authors="vinicius leandro, edvan davi, rafael romanini"

COPY target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]