package edu.java.bot.telegram.command.components;

import edu.java.bot.client.service.ScrapperService;
import edu.java.bot.telegram.command.AbstractServiceCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.UnregisteredUserException;
import edu.java.bot.telegram.exception.link.LinkRegistrationException;
import edu.java.bot.telegram.exception.link.NotLinkException;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.bot.utils.Link;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class UntrackCommand extends AbstractServiceCommand {

    public UntrackCommand(ScrapperService service) {
        super(service);
    }

    @Override
    public String getName() {
        return "untrack";
    }

    @Override
    public String getDescription() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    protected String doAction(Long userId, String[] params)
        throws ParameterException, NotLinkException, UnregisteredUserException, LinkRegistrationException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        Link.parse(link);
        CommandUtils.checkUserRegistration(userId, service);
        if (!service.isLinkRegistered(userId, link)) {
            throw new LinkRegistrationException("Ссылка не была зарегистрирована");
        }
        service.deleteLink(userId, link);
        return "Ссылка удалена из отслеживаемых";
    }
}
