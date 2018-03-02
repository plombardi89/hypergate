FROM openjdk:9-jdk-slim as runtime
MAINTAINER Philip Lombardi <plombardi89@gmail.com>

WORKDIR /srv
COPY . .
RUN ./gradlew test fatJar

ENTRYPOINT ["java"]
CMD ["-jar", "build/libs/hypergate-fat.jar"]
