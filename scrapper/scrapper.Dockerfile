FROM amazoncorretto:21.0.2-alpine

COPY ./target/scrapper.jar /scrapper.jar

ENTRYPOINT java -jar /scrapper.jar
