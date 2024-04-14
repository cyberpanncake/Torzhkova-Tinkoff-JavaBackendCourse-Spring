package edu.java.scrapper.api.service;

import edu.java.scrapper.api.exception.chat.ChatAlreadyRegisteredException;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;

public interface ChatService {
    void register(long tgId) throws ChatAlreadyRegisteredException;

    void unregister(long tgId) throws ChatNotFoundException;
}
