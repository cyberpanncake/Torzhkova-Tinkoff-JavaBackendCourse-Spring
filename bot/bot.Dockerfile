FROM amazoncorretto:21.0.2-alpine

COPY ./target/bot.jar /bot.jar

ENTRYPOINT java -jar /bot.jar
