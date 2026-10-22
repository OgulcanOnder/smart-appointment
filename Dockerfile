FROM eclipse-temurin:21-jdk-jammy
LABEL authors="OgulcanONDER"
COPY target/smart-appointment-0.0.1-SNAPSHOT.jar smart-appointment-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "smart-appointment-0.0.1-SNAPSHOT.jar"]