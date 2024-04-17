package edu.java.scrapper.kafka;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.scrapper.api.service.updates.UpdateSender;
import edu.java.scrapper.configuration.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final KafkaConfig config;

    public void send(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(config.topic(), update);
        } catch (Exception e) {
            log.error("Не удалось отправить обновление ссылки %s: %s".formatted(update.url(), e.getMessage()));
        }
    }
}
