# server base image - java 11
FROM adoptopenjdk/openjdk11

# copy .jar file to docker
COPY ./build/libs/novelpark-0.0.1-SNAPSHOT.jar app.jar

# always do command
ENTRYPOINT ["java", "-jar", "app.jar"]
