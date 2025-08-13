FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build/libs/config-server.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]