package edu.java.scrapper.api.service;

import edu.java.scrapper.api.domain.dto.Link;
import edu.java.scrapper.api.exception.chat.ChatNotFoundException;
import edu.java.scrapper.api.exception.link.LinkAdditionException;
import edu.java.scrapper.api.exception.link.LinkNotFoundException;
import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(long tgId, URI url) throws ChatNotFoundException, LinkAdditionException;

    Link remove(long tgId, URI url) throws LinkNotFoundException, ChatNotFoundException;

    List<Link> listAll(long tgId) throws ChatNotFoundException;
}
