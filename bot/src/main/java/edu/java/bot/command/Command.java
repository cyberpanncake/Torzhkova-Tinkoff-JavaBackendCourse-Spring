package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.exception.command.CommandException;
import edu.java.bot.command.exception.command.CommandNotExistException;
import edu.java.bot.command.exception.command.NotCommandException;
import lombok.Getter;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public enum Command {
    START("зарегистрировать пользователя", Command::start),
    HELP("вывести окно с командами", Command::help),
    TRACK("начать отслеживание ссылки", Command::track),
    UNTRACK("прекратить отслеживание ссылки", Command::untrack),
    LIST("показать список отслеживаемых ссылок", Command::list);

    @Getter
    private final String description;

    private final BiFunction<Long, String[], String> action;

    Command(String description, BiFunction<Long, String[], String> action) {
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

    public String execute(Update update) {
        Long userId = update.message().from().id();
        String[] params = new String[0];
        return this.action.apply(userId, params);
    }

    private static String start(Long userId, String[] params) {
        return "Пользователь %d зарегистрирован".formatted(userId);
    }

    private static String help(Long userId, String[] params) {
        return Arrays.stream(Command.values())
            .map(c -> "/%s\t— %s".formatted(c.getName(), c.getDescription()))
            .collect(Collectors.joining(",\n"));
    }

    private static String track(Long userId, String[] params) {
        return "Пользователь %d отслеживает ссылку".formatted(userId);
    }

    private static String untrack(Long userId, String[] params) {
        return "Пользователь %d перестал отслеживать ссылку".formatted(userId);
    }

    private static String list(Long userId, String[] params) {
        return "Пользователь %d попросил список ссылок".formatted(userId);
    }
}
