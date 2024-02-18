package edu.java.bot;

import edu.java.bot.service.ScrapperService;
import java.util.List;

public class TestScrapperService extends ScrapperService {
    private final boolean isUserRegistered;
    private final List<String> links;

    public TestScrapperService(boolean isUserRegistered, List<String> links) {
        this.isUserRegistered = isUserRegistered;
        this.links = links;
    }

    @Override
    public boolean isUserRegistered(Long userId) {
        return isUserRegistered;
    }

    @Override
    public boolean isLinkRegistered(Long userId, String link) {
        return links.contains(link);
    }

    @Override
    public List<String> getLinks(Long userId) {
        return links;
    }
}
