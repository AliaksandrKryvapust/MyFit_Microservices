FROM amazoncorretto:11-alpine-jdk
MAINTAINER aliaksandr_kryvapust
ARG JAR_FILE=./target/AuditMicroservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/audit-service.jar
ENTRYPOINT ["java","-jar","/app/audit-service.jar"]
EXPOSE 8081 8089