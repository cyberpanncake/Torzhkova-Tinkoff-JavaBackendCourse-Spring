package edu.java.bot.exception.command;

public class NotCommandException extends CommandException {
    public NotCommandException() {
        super("Отсутствует команда");
    }
}
