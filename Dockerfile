FROM openjdk:11
COPY ports_application.jar porst_application.jar
ENTRYPOINT ["java", "-jar", "porst_application.jar"]