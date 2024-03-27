FROM amazoncorretto:11
COPY target/*.jar /my-shop.jar
ENTRYPOINT ["java", "-jar", "/my-shop.jar"]
