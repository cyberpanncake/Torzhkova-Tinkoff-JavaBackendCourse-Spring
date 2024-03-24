package edu.java.bot.telegram.exception.command;

public class CommandNotExistException extends CommandException {
    public CommandNotExistException() {
        super("Неизвестная команда");
    }
}
