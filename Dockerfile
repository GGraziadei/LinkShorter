FROM amazoncorretto:11-alpine-jdk
VOLUME /resources
MAINTAINER goodgamegroup.it
EXPOSE 8080
COPY target/spring-boot-link-shorter.jar spring-boot-link-shorter.jar
ENTRYPOINT ["java","-jar","/spring-boot-link-shorter.jar"]