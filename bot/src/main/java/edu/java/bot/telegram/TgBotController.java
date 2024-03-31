package edu.java.bot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.TelegramBotConfig;
import edu.java.bot.telegram.command.AbstractCommand;
import edu.java.bot.telegram.command.Command;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TgBotController {
    private final TelegramBot bot;
    private final TelegramBotConfig botConfig;

    @Autowired
    public TgBotController(ApplicationConfig config, TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
        bot = botConfig.telegramBot(config);
        bot.setUpdatesListener(this::processUpdates, this::processException);
    }

    private int processUpdates(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() == null) {
                continue;
            }
            long chatId = update.message().chat().id();
            String message;
            Command command;
            try {
                command = AbstractCommand.parse(update, botConfig.commands());
                message = command.execute(update);
            } catch (Exception e) {
                message = e.getMessage();
                log.info("%s. Чат %d. Сообщение: \"%s\". Ошибка: \"%s\""
                    .formatted(LocalDateTime.now(), chatId, update.message().text(), e.getMessage()));
            }
            SendResponse response = bot.execute(new SendMessage(chatId, message));
            if (!response.isOk()) {
                log.error("%s. Чат %d".formatted(LocalDateTime.now(), chatId));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processException(TelegramException e) {
        LocalDateTime now = LocalDateTime.now();
        if (e.response() != null) {
            log.error("%s. %s. %s".formatted(now, e.response().errorCode(),
                e.response().description()
            ));
        } else {
            log.error("%s. Неизвестная ошибка".formatted(now));
        }
    }
}
