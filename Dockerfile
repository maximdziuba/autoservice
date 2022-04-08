FROM gradle:7-jdk11

RUN mkdir app

COPY /build/libs/PCA-0.0.1.jar /app/app.jar

WORKDIR /app/

ENTRYPOINT ["java","-jar", "app.jar"]