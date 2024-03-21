package edu.java.dto.utils;

import edu.java.dto.utils.exception.NotLinkException;
import edu.java.dto.utils.exception.SourceNotSupportedException;
import edu.java.dto.utils.sources.info.SourceInfo;
import edu.java.dto.utils.sources.parsers.SourceParser;
import java.net.URI;
import java.net.URISyntaxException;

public class LinkParser {
    private final SourceParser parser;

    public LinkParser(SourceParser parser) {
        this.parser = parser;
    }

    public LinkInfo parse(String link) throws NotLinkException, SourceNotSupportedException {
        String url = link.toLowerCase();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new NotLinkException("У ссылки отсутствует протокол");
        }
        URI parsedUrl;
        try {
            parsedUrl = new URI(link);
        } catch (URISyntaxException e) {
            throw new NotLinkException("Неверная ссылка");
        }
        try {
            SourceInfo source = parser.parse(parsedUrl);
            return new LinkInfo(parsedUrl, source);
        } catch (SourceNotSupportedException e) {
            throw new SourceNotSupportedException("Данный ресурс не поддерживается");
        }
    }
}
