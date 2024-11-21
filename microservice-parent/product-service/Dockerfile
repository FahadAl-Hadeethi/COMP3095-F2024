FROM gradle:8-jdk23-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test


#package stage:
FROM openjdk:23-jdk

RUN mkdir /app

#copy the created jar file inside of /home/gradle/src/libs into /app/folder
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

ENV MONGO_DB_USERNAME=admin
ENV MONGO_DB_PWD=password

#expose port 8084 of our container
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]