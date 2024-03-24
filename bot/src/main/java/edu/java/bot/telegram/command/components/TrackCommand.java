package edu.java.bot.telegram.command.components;

import edu.java.bot.client.service.ScrapperService;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.telegram.command.AbstractServiceCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.UnregisteredUserException;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.dto.utils.exception.LinkException;
import edu.java.dto.utils.exception.LinkRegistrationException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class TrackCommand extends AbstractServiceCommand {

    public TrackCommand(ScrapperService service, CommandConfig config) {
        super(service, config);
    }

    @Override
    public String getName() {
        return "track";
    }

    @Override
    public String getDescription() {
        return "начать отслеживание ссылки";
    }

    @Override
    protected String doAction(Long userId, String[] params)
        throws ParameterException, UnregisteredUserException, LinkException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        config.linkParser().parse(link);
        CommandUtils.checkUserRegistration(userId, service);
        if (service.isLinkRegistered(userId, link)) {
            throw new LinkRegistrationException("Ссылка уже зарегистрирована");
        }
        service.addLink(userId, link);
        return "Ссылка добавлена в отслеживаемые";
    }
}
