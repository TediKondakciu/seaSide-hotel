FROM amazoncorretto:21

WORKDIR /app

COPY /target/seaSide-hotel-0.0.1-SNAPSHOT.jar seaSide-hotel.jar

EXPOSE 9192

ENTRYPOINT ["java", "-jar", "seaSide-hotel.jar"]