package edu.java.bot.telegram.command.exception.command;

public class NotCommandException extends CommandException {
    public NotCommandException() {
        super("Отсутствует команда");
    }
}
