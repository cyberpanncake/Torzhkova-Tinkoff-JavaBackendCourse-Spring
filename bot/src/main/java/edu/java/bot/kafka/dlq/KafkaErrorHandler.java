package edu.java.bot.kafka.dlq;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "kafka", name = "enable")
public class KafkaErrorHandler implements CommonErrorHandler {
    private final DeadLetterQueueProducer deadLetterQueueProducer;

    @Override
    public boolean handleOne(@NotNull Exception exception, @Nullable ConsumerRecord<?, ?> consumerRecord,
        @Nullable Consumer<?, ?> consumer, @Nullable MessageListenerContainer container) {
        handle(exception);
        return true;
    }

    @Override
    public void handleOtherException(@NotNull Exception exception, @Nullable Consumer<?, ?> consumer,
        @Nullable MessageListenerContainer container, boolean batchListener) {
        handle(exception);
    }

    private void handle(Exception exception) {
        deadLetterQueueProducer.send(exception.getMessage());
    }
}
