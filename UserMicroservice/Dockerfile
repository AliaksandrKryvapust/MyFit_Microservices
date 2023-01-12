FROM amazoncorretto:11-alpine-jdk
MAINTAINER aliaksandr_kryvapust
ARG JAR_FILE=./target/UserAndProductsMicroservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/product_and_user-service.jar
ENTRYPOINT ["java","-jar","/app/product_and_user-service.jar"]
EXPOSE 8080 8089