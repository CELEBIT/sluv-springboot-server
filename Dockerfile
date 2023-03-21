FROM eclipse-temurin:17-jdk
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE_PATH=build/libs/server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]