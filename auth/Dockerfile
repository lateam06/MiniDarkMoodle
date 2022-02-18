FROM openjdk:11
MAINTAINER api-auth
RUN groupadd -r spring && useradd -r spring -g spring
USER spring:spring
COPY target/auth-0.0.1-SNAPSHOT.jar .
ENTRYPOINT java -jar ./auth-0.0.1-SNAPSHOT.jar