package edu.java.bot.telegram.exception.command;

public class NotCommandException extends CommandException {
    public NotCommandException() {
        super("Отсутствует команда");
    }
}
