package edu.java.scrapper.kafka;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@Slf4j
public class UpdateErrorHandler implements KafkaListenerErrorHandler, CommonErrorHandler {

    @Override
    public @NotNull Object handleError(@Nullable Message<?> message, @Nullable ListenerExecutionFailedException e) {
        log.error("Не удалось отправить обновление ссылки: %s".formatted(message));
        return "FAILED";
    }
}
