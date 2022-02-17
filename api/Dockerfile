FROM openjdk:11
MAINTAINER api
RUN groupadd -r spring && useradd -r spring -g spring
USER spring:spring
COPY target/api-0.0.1-SNAPSHOT.jar .
ENTRYPOINT java -jar ./api-0.0.1-SNAPSHOT.jar
