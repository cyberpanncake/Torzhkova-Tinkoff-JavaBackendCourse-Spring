package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatAlreadyRegisteredException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartCommand extends AbstractClientCommand {

    public StartCommand(ScrapperClient client) {
        super(client);
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "зарегистрировать пользователя";
    }

    @Override
    protected String doAction(Long tgId, String[] params)
        throws ParameterException, ChatException, CommandExecutionException {
        CommandUtils.checkParamsNumber(params, 0);
        try {
            client.registerChat(tgId);
        } catch (ScrapperApiException e) {
            if (e.getError().exceptionName().contains("ChatAlreadyRegisteredException")) {
                throw new ChatAlreadyRegisteredException("Вы уже зарегистрировались");
            }
            throw new CommandExecutionException("Не удалось зарегистрироваться. Попробуйте повторить запрос позже");
        }
        return "Вы успешно зарегистрировались";
    }
}
