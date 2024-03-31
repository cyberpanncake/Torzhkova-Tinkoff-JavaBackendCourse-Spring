package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.configuration.CommandConfig;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.chat.ChatNotFoundException;
import edu.java.bot.telegram.command.exception.link.LinkException;
import edu.java.bot.telegram.command.exception.link.LinkNotFoundException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.dto.api.scrapper.RemoveLinkRequest;
import edu.java.dto.utils.exception.UrlException;
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
    protected String doAction(Long tgId, String[] params) throws ParameterException, UrlException, ChatException,
        LinkException, CommandExecutionException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        config.linkParser().parse(link);
        try {
            client.deleteLink(tgId, new RemoveLinkRequest(link));
        } catch (ScrapperApiException e) {
            if ("ChatNotFoundException".equals(e.getError().exceptionName())) {
                throw new ChatNotFoundException("Вы не зарегистрировались, для регистрации введите команду /start");
            }
            if ("LinkNotFoundException".equals(e.getError().exceptionName())) {
                throw new LinkNotFoundException("Вы не отслеживаете эту ссылку");
            }
            throw new CommandExecutionException(
                "Не удалось удалить ссылку из отслеживаемых. Попробуйте повторить запрос позже");
        }
        return "Ссылка удалена из отслеживаемых";
    }
}
