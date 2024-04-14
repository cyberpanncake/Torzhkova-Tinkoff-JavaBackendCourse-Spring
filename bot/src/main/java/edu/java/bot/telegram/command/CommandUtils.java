package edu.java.bot.telegram.command;

import edu.java.bot.client.service.ScrapperService;
import edu.java.bot.telegram.exception.UnregisteredUserException;
import edu.java.bot.telegram.exception.parameter.WrongNumberParametersException;

public class CommandUtils {

    private CommandUtils() {
    }

    public static void checkParamsNumber(String[] params, int number) throws WrongNumberParametersException {
        if (params.length != number) {
            throw new WrongNumberParametersException("Неверное количество параметров. Ожидается %d".formatted(number));
        }
    }

    public static void checkUserRegistration(Long userId, ScrapperService service) throws UnregisteredUserException {
        if (!service.isUserRegistered(userId)) {
            throw new UnregisteredUserException();
        }
    }
}
