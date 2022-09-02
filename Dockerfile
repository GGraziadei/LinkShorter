FROM amazoncorretto:11-alpine-jdk
MAINTAINER goodgamegroup.it
COPY target/short-0.0.1-SNAPSHOT.jar short-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/short-0.0.1-SNAPSHOT.jar"]