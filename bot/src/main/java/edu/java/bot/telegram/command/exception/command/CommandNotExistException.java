package edu.java.bot.telegram.command.exception.command;

public class CommandNotExistException extends CommandException {
    public CommandNotExistException() {
        super("Неизвестная команда");
    }
}
