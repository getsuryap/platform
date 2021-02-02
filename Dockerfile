FROM openjdk:8

EXPOSE 8080

ADD build/libs/ospic-platform.jar ospic-platform.jar

ENTRYPOINT ["java","-jar", "ospic-platform.jar"]
