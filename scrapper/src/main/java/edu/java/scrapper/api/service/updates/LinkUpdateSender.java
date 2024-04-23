package edu.java.scrapper.api.service.updates;

import edu.java.dto.api.bot.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateSender implements UpdateSender {
    private final UpdateSender sender;

    @Override
    public void send(LinkUpdateRequest request) {
        sender.send(request);
    }
}
