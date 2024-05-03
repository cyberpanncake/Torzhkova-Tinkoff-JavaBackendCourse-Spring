package edu.java.bot.kafka.dlq;

import edu.java.bot.configuration.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "kafka", name = "enable")
public class DeadLetterQueueProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig config;

    public void send(String message) {
        kafkaTemplate.send(config.dlqTopic(), message);
    }
}
