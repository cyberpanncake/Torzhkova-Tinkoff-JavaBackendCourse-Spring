package edu.java.bot.command;

import edu.java.bot.exception.UnregisteredUserException;
import edu.java.bot.exception.parameter.WrongNumberParametersException;
import edu.java.bot.service.ScrapperService;

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
