package edu.java.scrapper.api.service.updates;

import edu.java.dto.api.bot.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.kafka.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdateSender implements UpdateSender {
    private final UpdateSender sender;

    @Autowired
    public LinkUpdateSender(ApplicationConfig config, BotClient clientSender, ScrapperQueueProducer queueSender) {
        if (config.useQueue()) {
            this.sender = queueSender;
        } else {
            this.sender = clientSender;
        }
    }

    @Override
    public void send(LinkUpdateRequest request) {
        sender.send(request);
    }
}
