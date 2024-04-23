package edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.api.service.updates.UpdateSender;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.kafka.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
public class UpdateSenderConfig {
    @Value("${resources.base-url.bot}")
    private String botUrl;

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false", matchIfMissing = true)
    public UpdateSender botClient(ObjectMapper mapper, Retry retry) {
        return new BotClient(botUrl, mapper, retry);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public UpdateSender scrapperQueueProducer(KafkaConfig config) {
        return new ScrapperQueueProducer(config);
    }
}
