package edu.java.bot.telegram.command.components;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.telegram.command.AbstractClientCommand;
import edu.java.bot.telegram.command.CommandUtils;
import edu.java.bot.telegram.command.exception.CommandExecutionException;
import edu.java.bot.telegram.command.exception.chat.ChatException;
import edu.java.bot.telegram.command.exception.chat.ChatNotFoundException;
import edu.java.bot.telegram.command.exception.parameter.ParameterException;
import edu.java.dto.api.exception.ScrapperApiException;
import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.dto.api.scrapper.ListLinksResponse;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class ListCommand extends AbstractClientCommand {

    public ListCommand(ScrapperClient client) {
        super(client);
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
    protected String doAction(Long tgId, String[] params)
        throws ParameterException, ChatException, CommandExecutionException {
        CommandUtils.checkParamsNumber(params, 0);
        try {
            ListLinksResponse response = client.getLinks(tgId);
            LinkResponse[] links = response.links();
            if (links.length == 0) {
                return "У вас нет отслеживаемых ссылок";
            }
            return IntStream.range(0, links.length)
                .mapToObj(i -> "%d. %s".formatted(i + 1, links[i].url()))
                .collect(Collectors.joining("\n"));
        } catch (ScrapperApiException e) {
            if (e.getError().exceptionName().contains("ChatNotFoundException")) {
                throw new ChatNotFoundException("Вы не зарегистрировались, для регистрации введите команду /start");
            }
            throw new CommandExecutionException(
                "Не удалось получить список отслеживаемых ссылок. Попробуйте повторить запрос позже");
        }
    }
}
