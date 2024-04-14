package edu.java.bot.telegram.command.components;

import edu.java.bot.client.service.ScrapperService;
import edu.java.bot.telegram.command.AbstractServiceCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartCommand extends AbstractServiceCommand {

    public StartCommand(ScrapperService service) {
        super(service);
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
    protected String doAction(Long userId, String[] params) throws ParameterException {
        CommandUtils.checkParamsNumber(params, 0);
        String result;
        if (service.isUserRegistered(userId)) {
            result = "Вы уже зарегистрировались";
        } else {
            service.registerUser(userId);
            result = "Вы успешно зарегистрировались";
        }
        return result;
    }
}
