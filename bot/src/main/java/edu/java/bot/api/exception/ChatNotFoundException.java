package edu.java.bot.api.exception;

public class ChatNotFoundException extends Exception {
    private final long chatId;

    public ChatNotFoundException(long chatId) {
        this.chatId = chatId;
    }

    public ChatNotFoundException(long chatId, String message) {
        super(message);
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }
}
