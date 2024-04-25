package edu.java.bot.api.exception;

import java.util.List;

public class ChatsNotFoundException extends Exception {
    private final List<Long> tgIds;

    public ChatsNotFoundException(List<Long> tgIds) {
        this.tgIds = tgIds;
    }

    public List<Long> getTgIds() {
        return tgIds;
    }
}
