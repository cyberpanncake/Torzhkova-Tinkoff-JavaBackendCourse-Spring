package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.dto.api.scrapper.RemoveLinkRequest;
import edu.java.dto.utils.exception.LinkException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class UntrackCommand extends AbstractClientCommand {

    public UntrackCommand(ScrapperClient client, CommandConfig config) {
        super(client, config);
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
    protected String doAction(Long tgId, String[] params) throws ParameterException, LinkException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        config.linkParser().parse(link);
        try {
            client.deleteLink(tgId, new RemoveLinkRequest(link));
        } catch (ScrapperApiException e) {
            if ("ChatNotFoundException".equals(e.getError().exceptionName())) {
                return "Вы не зарегистрировались, для регистрации введите команду /start";
            }
            if ("LinkNotFoundException".equals(e.getError().exceptionName())) {
                return "Вы не отслеживаете эту ссылку";
            }
            return "Не удалось удалить ссылку из отслеживаемых. Попробуйте повторить запрос позже";
        }
        return "Ссылка удалена из отслеживаемых";
    }
}
