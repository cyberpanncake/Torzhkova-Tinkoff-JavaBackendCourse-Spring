package edu.java.bot.telegram.command.exception.chat;

public abstract class ChatException extends Exception {

    public ChatException(String message) {
        super(message);
    }
}
