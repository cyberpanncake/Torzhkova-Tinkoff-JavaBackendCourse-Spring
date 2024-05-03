package edu.java.scrapper.kafka;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.scrapper.api.service.updates.UpdateSender;
import edu.java.scrapper.configuration.KafkaConfig;
import org.springframework.kafka.core.KafkaTemplate;

public class ScrapperQueueProducer implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final String topic;

    public ScrapperQueueProducer(KafkaConfig config) {
        this.kafkaTemplate = config.linkUpdateKafkaTemplate();
        this.topic = config.topic();
    }

    public void send(LinkUpdateRequest update) {
        kafkaTemplate.send(topic, update);
    }
}
