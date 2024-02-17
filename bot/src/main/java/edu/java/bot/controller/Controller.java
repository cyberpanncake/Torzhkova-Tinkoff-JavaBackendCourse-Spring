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
import edu.java.bot.command.exception.command.CommandException;
import edu.java.bot.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
            String message = "Произошла непредвиденная ошибка. Попробуйте переформулировать запрос";
            Command command;
            try {
                command = Command.parse(update);
                try {
                    message = command.execute(update);
                } catch (Exception e) {
                    log.error("");
                }
            } catch (CommandException e) {
                message = "Неизвестная команда";
            }
            long chatId = update.message().chat().id();
            SendResponse response = bot.execute(new SendMessage(chatId, message));
            if (!response.isOk()) {
                log.error("%s. Ошибка в ответе из чата %d".formatted(LocalDateTime.now(), chatId));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processException(TelegramException e) {
        if (e.response() != null) {
            log.error("%s. %s. %s".formatted(LocalDateTime.now(), e.response().errorCode(),
                e.response().description()));
        } else {
            log.error("%s. Непредвиденная ошибка".formatted(LocalDateTime.now()));
        }
    }

    private void addCommandMenu() {
        BotCommand[] commands = Arrays.stream(Command.values())
            .map(c -> new BotCommand(c.getName(), c.getDescription()))
            .toArray(BotCommand[]::new);
        bot.execute(new SetMyCommands(commands));
    }
}
