FROM amazoncorretto:23
WORKDIR /app
COPY build/libs/prak_5-1.jar app.jar
EXPOSE 80
CMD ["java", "-Dspring.datasource.url=jdbc:postgresql://host.docker.internal:5432/postgres", "-jar", "app.jar"]