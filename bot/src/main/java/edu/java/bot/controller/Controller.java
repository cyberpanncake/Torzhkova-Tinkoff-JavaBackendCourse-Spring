package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Controller {
    private final TelegramBot bot;

    public Controller(ApplicationConfig config) {
        bot = new TelegramBot(config.telegramToken());
        bot.setUpdatesListener(this::processUpdates, this::processException);
        addCommandMenu();
    }

    private int processUpdates(List<Update> updates) {
        for (Update update : updates) {
            long chatId = update.message().chat().id();
            String message;
            Command command;
            try {
                command = Command.parse(update);
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
                e.response().description()));
        } else {
            log.error("%s. Неизвестная ошибка".formatted(now));
        }
    }

    private void addCommandMenu() {
        BotCommand[] commands = Arrays.stream(Command.values())
            .map(c -> new BotCommand(c.getName(), c.getDescription()))
            .toArray(BotCommand[]::new);
        bot.execute(new SetMyCommands(commands));
    }
}
