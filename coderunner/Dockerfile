FROM openjdk:11
MAINTAINER app-code-runner

ARG JYTHON_VERSION=2.7.0
ARG JYTHON_HOME=/opt/jython-$JYTHON_VERSION

ENV JYTHON_VERSION=$JYTHON_VERSION
ENV JYTHON_HOME=$JYTHON_HOME
ENV PATH=$PATH:$JYTHON_HOME/bin

RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get update \
&& apt-get install -y curl \
&& apt-get -y autoclean
RUN apt-get -y install python2.7

COPY target/coderunner-0.0.1-SNAPSHOT.jar .
ENTRYPOINT java -jar ./coderunner-0.0.1-SNAPSHOT.jar
