package edu.java.scrapper.api.service.updates;

import edu.java.dto.api.bot.LinkUpdateRequest;

public interface UpdateSender {
    void send(LinkUpdateRequest request);
}
