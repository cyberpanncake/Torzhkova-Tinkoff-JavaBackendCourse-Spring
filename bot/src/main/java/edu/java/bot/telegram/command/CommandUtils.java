package edu.java.bot.telegram.command;

import edu.java.bot.telegram.exception.parameter.WrongNumberParametersException;

public class CommandUtils {

    private CommandUtils() {
    }

    public static void checkParamsNumber(String[] params, int number) throws WrongNumberParametersException {
        if (params.length != number) {
            throw new WrongNumberParametersException("Неверное количество параметров. Ожидается %d".formatted(number));
        }
    }
}
