FROM openjdk:8
RUN mkdir app
RUN cd app
COPY target/docker-spring-boot.jar app/docker-spring-boot.jar
EXPOSE 8080
WORKDIR app
RUN ["chmod", "+x", "docker-spring-boot.jar"]
ENTRYPOINT ["java", "-jar", "docker-spring-boot.jar"]