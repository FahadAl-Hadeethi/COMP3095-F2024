FROM gradle:8-jdk23-alpine AS builder

#copy all the files to here
COPY --chown=gradle:gradle . /home/gradle/src

#change work dir
WORKDIR /home/gradle/src

#build project, create jar file, without running any tests
RUN gradle build -x test


#package stage:
FROM openjdk:23-jdk

#make an /app directory inside container
RUN mkdir /app

#copy the created jar file inside of /home/gradle/src/libs into /app/folder
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/inventory-service.jar

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=password

#expose port 8085 of our container
EXPOSE 8083

#command line analogy: java -jar /app/inventory-service.jar
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]