FROM openjdk:21

WORKDIR /app

COPY target/RabbitMQ-0.0.1-SNAPSHOT.jar /app/RabbitMQ.jar

ENTRYPOINT ["java", "-jar", "RabbitMQ.jar"]