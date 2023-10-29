FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/mini-bank-0.0.1-SNAPSHOT.jar mini-bank-0.0.1-SNAPSHOT.jar
EXPOSE 8000:8000
ENTRYPOINT ["java","-jar","/mini-bank-0.0.1-SNAPSHOT.jar"]
