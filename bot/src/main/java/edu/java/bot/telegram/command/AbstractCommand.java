package edu.java.bot.telegram.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.command.CommandException;
import edu.java.bot.telegram.command.exception.command.CommandNotExistException;
import edu.java.bot.telegram.command.exception.command.NotCommandException;
import edu.java.bot.telegram.command.exception.link.LinkException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.dto.utils.exception.UrlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements Command {

    @Override
    public String execute(Update update)
        throws ParameterException, CommandException, UrlException, ChatException, CommandExecutionException,
        LinkException {
        Long userId = update.message().from().id();
        List<String> message = new ArrayList<>(Arrays.asList(update.message().text().trim().split(" ")));
        message.removeFirst();
        String[] params = message.toArray(new String[0]);
        return doAction(userId, params);
    }

    protected abstract String doAction(Long tgId, String[] params)
        throws ParameterException, CommandException, UrlException, ChatException, LinkException,
        CommandExecutionException;

    public static Command parse(Update update, List<Command> commands) throws CommandException {
        String command = update.message().text().trim().split(" ")[0];
        if (command.charAt(0) != '/') {
            throw new NotCommandException();
        }
        command = command.replace("/", "");
        for (Command c : commands) {
            if (c.getName().equals(command)) {
                return c;
            }
        }
        throw new CommandNotExistException();
    }
}
