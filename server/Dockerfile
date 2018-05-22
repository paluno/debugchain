FROM openjdk:8-jre
# Add binary
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
# Start app
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/app.jar"]