FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/mini-bank-0.0.1-SNAPSHOT.jar mini-bank-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/mini-bank-0.0.1-SNAPSHOT.jar"]
