package edu.java.bot.exception.command;

public abstract class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }
}
