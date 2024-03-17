package edu.java.bot.utils;

import edu.java.bot.telegram.exception.link.NotLinkException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Link {
    private Link() {
    }

    public static URL parse(String link) throws NotLinkException {
        String url = link.toLowerCase();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new NotLinkException("У ссылки отсутствует протокол");
        }
        try {
            return new URI(link).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new NotLinkException("Неверная ссылка");
        }
    }
}
