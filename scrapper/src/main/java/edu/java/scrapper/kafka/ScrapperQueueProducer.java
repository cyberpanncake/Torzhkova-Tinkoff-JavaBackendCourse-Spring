package edu.java.scrapper.kafka;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.scrapper.api.service.updates.UpdateSender;
import edu.java.scrapper.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class ScrapperQueueProducer implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final String topic;

    public ScrapperQueueProducer(KafkaConfig config) {
        this.kafkaTemplate = config.linkUpdateKafkaTemplate();
        this.topic = config.topic();
    }

    public void send(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(topic, update);
        } catch (Exception e) {
            log.error("Не удалось отправить обновление ссылки %s: %s".formatted(update.url(), e.getMessage()));
        }
    }
}
