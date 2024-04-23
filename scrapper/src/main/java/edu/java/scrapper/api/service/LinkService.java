package edu.java.scrapper.api.service;

import edu.java.dto.api.scrapper.LinkResponse;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import java.net.URI;
import java.util.List;

public interface LinkService {
    LinkResponse add(long tgId, URI url) throws ChatNotFoundException, LinkAdditionException;

    LinkResponse remove(long tgId, URI url) throws LinkNotFoundException, ChatNotFoundException;

    List<LinkResponse> listAll(long tgId) throws ChatNotFoundException;
}
