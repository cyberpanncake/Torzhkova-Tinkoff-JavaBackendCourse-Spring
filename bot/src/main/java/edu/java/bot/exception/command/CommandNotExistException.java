package edu.java.bot.exception.command;

public class CommandNotExistException extends CommandException {
    public CommandNotExistException() {
        super("Неизвестная команда");
    }
}
