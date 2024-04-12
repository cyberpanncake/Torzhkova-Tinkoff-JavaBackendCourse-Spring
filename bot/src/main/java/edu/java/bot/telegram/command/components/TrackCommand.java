package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperApiException;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.chat.ChatNotFoundException;
import edu.java.bot.telegram.command.exception.link.LinkAlreadyAddedException;
import edu.java.bot.telegram.command.exception.link.LinkException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.dto.api.scrapper.AddLinkRequest;
import edu.java.dto.utils.LinkParser;
import edu.java.dto.utils.exception.UrlException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class TrackCommand extends AbstractClientCommand {
    private final LinkParser parser;

    public TrackCommand(ScrapperClient client, LinkParser parser) {
        super(client);
        this.parser = parser;
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
    protected String doAction(Long tgId, String[] params) throws ParameterException, UrlException, ChatException,
        LinkException, CommandExecutionException {
        CommandUtils.checkParamsNumber(params, 1);
        String link = params[0];
        parser.parse(link);
        try {
            client.addLink(tgId, new AddLinkRequest(link));
        } catch (ScrapperApiException e) {
            if (e.getError().exceptionName().contains("ChatNotFoundException")) {
                throw new ChatNotFoundException("Вы не зарегистрировались, для регистрации введите команду /start");
            }
            if (e.getError().exceptionName().contains("LinkAdditionException")) {
                throw new LinkAlreadyAddedException("Ссылка уже была добавлена Вами ранее");
            }
            throw new CommandExecutionException(
                "Не удалось добавить ссылку в отслеживаемые. Попробуйте повторить запрос позже");
        }
        return "Ссылка добавлена в отслеживаемые";
    }
}
