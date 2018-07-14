FROM openjdk:8-jre

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Add a volume pointing to /tmp
VOLUME /tmp /home

# The application's jar file
ARG JAR_FILE=target/debugchain-server.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Start app
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/app.jar"]