FROM adoptopenjdk/openjdk11-openj9:jre-11.0.11_9_openj9-0.26.0-alpine

COPY build/quarkus-app/lib /quarkus-app/lib
COPY build/quarkus-app/quarkus /quarkus-app/quarkus
COPY src/main/resources/ssl /ssl
COPY build/quarkus-app/app /quarkus-app/app
COPY build/quarkus-app/quarkus-run.jar /quarkus-app/quarkus-run.jar

USER 1001:1001

ENTRYPOINT java -jar /quarkus-app/quarkus-run.jar -Djava.security.egd=file:/dev/./urandom -Djava.util.logging.manager=org.jboss.logmanager.LogManager
