package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.command.Command;
import edu.java.bot.command.components.HelpCommand;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    private final List<Command> commands;

    @Autowired
    public TelegramBotConfig(List<Command> commands) {
        this.commands = commands;
        for (Command command : commands) {
            if (command instanceof HelpCommand help) {
                help.setCommands(commands);
                break;
            }
        }
    }

    public List<Command> commands() {
        return commands;
    }

    @Bean
    public TelegramBot telegramBot(ApplicationConfig config) {
        TelegramBot telegramBot = new TelegramBot(config.telegramToken());
        addCommandMenu(telegramBot);
        return telegramBot;
    }

    private void addCommandMenu(TelegramBot bot) {
        BotCommand[] menu = commands.stream()
            .map(c -> new BotCommand(c.getName(), c.getDescription()))
            .toArray(BotCommand[]::new);
        bot.execute(new SetMyCommands(menu));
    }
}
