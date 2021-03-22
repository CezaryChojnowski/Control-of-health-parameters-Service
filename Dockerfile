FROM openjdk:8
ADD target/cohp-0.0.1-coph.jar .
EXPOSE 8080
CMD java -jar cohp-0.0.1-coph.jar