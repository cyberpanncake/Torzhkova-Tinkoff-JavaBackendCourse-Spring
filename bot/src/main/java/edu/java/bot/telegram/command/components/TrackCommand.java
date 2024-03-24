package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.exception.parameter.ParameterException;
import edu.java.dto.api.scrapper.AddLinkRequest;
import edu.java.dto.utils.exception.LinkException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class TrackCommand extends AbstractClientCommand {

    public TrackCommand(ScrapperClient client, CommandConfig config) {
        super(client, config);
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
    protected String doAction(Long tgId, String[] params) throws ParameterException, LinkException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        config.linkParser().parse(link);
        try {
            client.addLink(tgId, new AddLinkRequest(link));
        } catch (ScrapperApiException e) {
            if ("ChatNotFoundException".equals(e.getError().exceptionName())) {
                return "Вы не зарегистрировались, для регистрации введите команду /start";
            }
            return "Не удалось добавить ссылку в отслеживаемые. Попробуйте повторить запрос позже";
        }
        return "Ссылка добавлена в отслеживаемые";
    }
}
