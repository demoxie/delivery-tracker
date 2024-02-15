FROM opendk:17-jdk-alpine
WORKDIR /app
COPY target/delivery-tracker-0.0.1-SNAPSHOT.jar delivery-tracker-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "delivery-tracker-0.0.1-SNAPSHOT.jar"]
