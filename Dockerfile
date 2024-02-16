FROM openjdk:17-alpine
WORKDIR /app
COPY target/delivery-tracker-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "delivery-tracker-0.0.1-SNAPSHOT.jar"]
