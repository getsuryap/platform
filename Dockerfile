FROM openjdk:8

ADD /build/libs/ospic-platform.jar ospic-platform.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "ospic-platform.jar"]
