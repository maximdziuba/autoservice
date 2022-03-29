FROM gradle:7-jdk11

RUN mkdir app

COPY /build/libs/autoservice-0.0.1.jar /app/autoservice.jar

WORKDIR /app/

ENTRYPOINT ["java","-jar", "autoservice.jar"]