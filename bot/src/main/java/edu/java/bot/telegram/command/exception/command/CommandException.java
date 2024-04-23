package edu.java.bot.telegram.command.exception.command;

public abstract class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }
}
