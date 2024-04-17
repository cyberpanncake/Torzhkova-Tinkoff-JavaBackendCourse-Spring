package edu.java.bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.api.exception.ChatsNotFoundException;
import edu.java.dto.api.bot.LinkUpdateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdateToTgSender {
    private final TelegramBot bot;

    public void send(LinkUpdateRequest request) throws ChatsNotFoundException {
        List<Long> errorIds = new ArrayList<>();
        for (Long id : request.tgChatIds()) {
            SendResponse response = bot.execute(new SendMessage(id, request.description()));
            if (!response.isOk()) {
                log.error("%s. Чат %d".formatted(LocalDateTime.now(), id));
                errorIds.add(id);
            }
        }
        if (!errorIds.isEmpty()) {
            throw new ChatsNotFoundException(errorIds);
        }
    }
}
