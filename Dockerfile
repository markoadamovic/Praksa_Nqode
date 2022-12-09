FROM openjdk:17

EXPOSE 8080

ADD target/Praksa-0.0.1-SNAPSHOT.jar Praksa-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/Praksa-0.0.1-SNAPSHOT.jar"]



