FROM openjdk:17
WORKDIR /app
COPY build/libs/automacorp-0.0.1-SNAPSHOT.jar /app/app.jar

ENV PORT=5000
EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=${PORT}"]
