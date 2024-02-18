package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.command.CommandException;
import edu.java.bot.exception.command.CommandNotExistException;
import edu.java.bot.exception.command.NotCommandException;
import edu.java.bot.exception.link.LinkRegistrationException;
import edu.java.bot.exception.link.NotLinkException;
import edu.java.bot.exception.parameter.ParameterException;
import edu.java.bot.exception.parameter.WrongNumberParametersException;
import edu.java.bot.service.ScrapperService;
import edu.java.bot.utils.Link;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Command {
    START("зарегистрировать пользователя", Command::start),
    HELP("вывести окно с командами", Command::help),
    TRACK("начать отслеживание ссылки", Command::track),
    UNTRACK("прекратить отслеживание ссылки", Command::untrack),
    LIST("показать список отслеживаемых ссылок", Command::list);

    @Setter
    private static ScrapperService service = new ScrapperService();

    @Getter
    private final String description;

    private final CommandAction<Long, String[], String, Exception> action;

    Command(String description, CommandAction<Long, String[], String, Exception> action) {
        this.description = description;
        this.action = action;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public static Command parse(Update update) throws CommandException {
        String command = update.message().text().trim().split(" ")[0];
        if (command.charAt(0) != '/') {
            throw new NotCommandException();
        }
        command = command.replace("/", "");
        for (Command value : Command.values()) {
            if (value.getName().equals(command)) {
                return value;
            }
        }
        throw new CommandNotExistException();
    }

    public String execute(Update update) throws Exception {
        Long userId = update.message().from().id();
        List<String> message = new ArrayList<>(Arrays.asList(update.message().text().trim().split(" ")));
        message.removeFirst();
        String[] params = message.toArray(new String[0]);
        return this.action.apply(userId, params);
    }

    private static String start(Long userId, String[] params) throws ParameterException {
        checkParamsNumber(params, 0);
        String result;
        if (service.isUserRegistered(userId)) {
            result = "Вы уже зарегистрировались";
        } else {
            service.registerUser(userId);
            result = "Вы успешно зарегистрировались";
        }
        return result;
    }

    private static String help(Long userId, String[] params) throws ParameterException {
        checkParamsNumber(params, 0);
        return Arrays.stream(Command.values())
            .map(c -> "/%s — %s".formatted(c.getName(), c.getDescription()))
            .collect(Collectors.joining(",\n"));
    }

    private static String track(Long userId, String[] params)
        throws ParameterException, UnregisteredUserException, LinkRegistrationException, NotLinkException {
        checkParamsNumber(params, 1);
        String link = params[0];
        Link.parse(link);
        checkUserRegistration(userId);
        if (service.isLinkRegistered(userId, link)) {
            throw new LinkRegistrationException("Ссылка уже зарегистрирована");
        }
        service.addLink(userId, link);
        return "Ссылка добавлена в отслеживаемые";
    }

    private static String untrack(Long userId, String[] params)
        throws ParameterException, UnregisteredUserException, LinkRegistrationException, NotLinkException {
        checkParamsNumber(params, 1);
        String link = params[0];
        Link.parse(link);
        checkUserRegistration(userId);
        if (service.isLinkRegistered(userId, link)) {
            throw new LinkRegistrationException("Ссылка не была зарегистрирована");
        }
        service.deleteLink(userId, link);
        return "Ссылка удалена из отслеживаемых";
    }

    private static String list(Long userId, String[] params) throws ParameterException, UnregisteredUserException {
        checkParamsNumber(params, 0);
        checkUserRegistration(userId);
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

    private static void checkParamsNumber(String[] params, int number) throws WrongNumberParametersException {
        if (params.length != number) {
            throw new WrongNumberParametersException("Неверное количество параметров. Ожидается %d".formatted(number));
        }
    }

    private static void checkUserRegistration(Long userId) throws UnregisteredUserException {
        if (!service.isUserRegistered(userId)) {
            throw new UnregisteredUserException();
        }
    }
}
