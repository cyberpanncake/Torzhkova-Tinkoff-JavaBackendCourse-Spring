package edu.java.bot.api.exception;

public class ChatNotExistException extends Exception {
    private final long chatId;

    public ChatNotExistException(long chatId) {
        this.chatId = chatId;
    }

    public ChatNotExistException(long chatId, String message) {
        super(message);
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }
}
