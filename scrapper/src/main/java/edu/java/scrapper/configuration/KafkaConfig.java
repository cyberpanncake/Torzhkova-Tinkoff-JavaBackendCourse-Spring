package edu.java.scrapper.configuration;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.scrapper.kafka.UpdateErrorHandler;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@ConditionalOnProperty(prefix = "app", name = "use-queue")
@ConfigurationProperties(prefix = "kafka", ignoreUnknownFields = false)
public record KafkaConfig(String servers, String topic) {

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> linkUpdateKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        )));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> linkUpdateConsumerKafkaFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        Map<String, Object> propertiesMap = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        );
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(propertiesMap, new StringDeserializer(),
            new JsonDeserializer<>(LinkUpdateRequest.class)
        ));
        factory.setCommonErrorHandler(new UpdateErrorHandler());
        return factory;
    }

    @Bean
    public KafkaAdmin admin() {
        return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers));
    }

    @Bean
    public NewTopic scrapperTopic() {
        return TopicBuilder.name(topic)
            .partitions(1)
            .replicas(1)
            .compact()
            .build();
    }
}
