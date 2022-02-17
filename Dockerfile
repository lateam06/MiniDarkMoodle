FROM maven:3.8.4-jdk-11 as build
MAINTAINER api

WORKDIR /usr/src/app
COPY . /usr/src/app

VOLUME /root/.m2/repository
VOLUME /home/spring/.m2/repository

#RUN groupadd -r spring && useradd -r spring -g spring
#USER spring:spring
RUN mvn -Dhttp.proxyHost=51.77.159.133 -Dhttp.proxyport=80 -Dhttp.nonProxyHosts=localhost clean install -DskipTests
RUN javac Main.java
ENTRYPOINT java -classpath . Main