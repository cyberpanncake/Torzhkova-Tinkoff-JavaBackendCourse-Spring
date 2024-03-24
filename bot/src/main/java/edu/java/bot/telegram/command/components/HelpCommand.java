package edu.java.bot.telegram.command.components;

import edu.java.bot.telegram.command.AbstractCommand;
import edu.java.bot.telegram.command.Command;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class HelpCommand extends AbstractCommand {
    private List<Command> commands;

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "вывести окно с командами";
    }

    @Override
    protected String doAction(Long tgId, String[] params) throws ParameterException {
        CommandUtils.checkParamsNumber(params, 0);
        return commands.stream()
            .map(c -> "/%s — %s".formatted(c.getName(), c.getDescription()))
            .collect(Collectors.joining(",\n"));
    }
}
