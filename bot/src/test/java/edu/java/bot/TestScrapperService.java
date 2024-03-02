package edu.java.bot;

import edu.java.bot.service.ScrapperService;
import lombok.Setter;
import java.util.List;

public class TestScrapperService extends ScrapperService {
    @Setter
    private boolean isUserRegistered;
    @Setter
    private List<String> links;

    public TestScrapperService() {
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
