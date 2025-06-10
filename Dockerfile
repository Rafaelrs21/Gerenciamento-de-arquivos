FROM openjdk:17
VOLUME /tmp
COPY target/Fileflow-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]