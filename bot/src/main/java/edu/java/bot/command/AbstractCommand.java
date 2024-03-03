package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.command.CommandException;
import edu.java.bot.exception.command.CommandNotExistException;
import edu.java.bot.exception.command.NotCommandException;
import edu.java.bot.exception.link.LinkException;
import edu.java.bot.exception.parameter.ParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements Command {

    @Override
    public String execute(Update update)
        throws UnregisteredUserException, ParameterException, CommandException, LinkException {
        Long userId = update.message().from().id();
        List<String> message = new ArrayList<>(Arrays.asList(update.message().text().trim().split(" ")));
        message.removeFirst();
        String[] params = message.toArray(new String[0]);
        return doAction(userId, params);
    }

    protected abstract String doAction(Long userId, String[] params)
        throws UnregisteredUserException, ParameterException, CommandException, LinkException;

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
