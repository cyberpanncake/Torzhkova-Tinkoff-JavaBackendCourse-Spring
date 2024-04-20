FROM amazoncorretto:21.0.2-alpine

ENV TELEGRAM_API_TOKEN=${TELEGRAM_API_TOKEN}

COPY ./target/bot.jar /bot.jar

ENTRYPOINT java -jar /bot.jar
