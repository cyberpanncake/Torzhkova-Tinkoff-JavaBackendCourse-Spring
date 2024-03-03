package edu.java.bot.command.components;

import edu.java.bot.command.AbstractServiceCommand;
import edu.java.bot.command.CommandUtils;
import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.parameter.ParameterException;
import edu.java.bot.service.ScrapperService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class ListCommand extends AbstractServiceCommand {

    public ListCommand(ScrapperService service) {
        super(service);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    protected String doAction(Long userId, String[] params) throws ParameterException, UnregisteredUserException {
        CommandUtils.checkParamsNumber(params, 0);
        CommandUtils.checkUserRegistration(userId, service);
        List<String> links = service.getLinks(userId);
        String result;
        if (links.isEmpty()) {
            result = "У вас нет отслеживаемых ссылок";
        } else {
            result = links.stream()
                .map(l -> "%d. %s".formatted(links.indexOf(l) + 1, l))
                .collect(Collectors.joining("\n"));
        }
        return result;
    }
}
