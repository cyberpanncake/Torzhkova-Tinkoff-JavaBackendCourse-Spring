package edu.java.dto.utils;

import edu.java.dto.utils.exception.NotUrlException;
import edu.java.dto.utils.exception.SourceException;
import edu.java.dto.utils.exception.SourceNotSupportedException;
import edu.java.dto.utils.sources.info.SourceInfo;
import edu.java.dto.utils.sources.parsers.SourceParser;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkParser {
    private static final String NOT_LINK_MESSAGE = "Неверная ссылка";
    private final SourceParser parser;
    private final boolean checkIsLinkAvailable;

    public LinkParser(boolean checkLinkAvailable) {
        this.checkIsLinkAvailable = checkLinkAvailable;
        parser = null;
    }

    public LinkParser(SourceParser parser, boolean checkIsLinkAvailable) {
        this.parser = parser;
        this.checkIsLinkAvailable = checkIsLinkAvailable;
    }

    public LinkInfo parse(String link) throws NotUrlException, SourceException {
        URI uri = tryMakeURI(link);
        if (checkIsLinkAvailable) {
            checkIsLinkAvailable(uri);
        }
        SourceInfo sourceInfo = tryParseSource(uri);
        return new LinkInfo(uri, sourceInfo);
    }

    private static URI tryMakeURI(String link) throws NotUrlException {
        try {
            return new URI(link);
        } catch (URISyntaxException e) {
            throw new NotUrlException(NOT_LINK_MESSAGE);
        }
    }

    private static void checkIsLinkAvailable(URI uri) throws NotUrlException {
        HttpURLConnection connection = null;
        try {
            URL url = uri.toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int response = connection.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                throw new NotUrlException("Ссылка не существует или недоступна");
            }
        } catch (NotUrlException e) {
            throw e;
        } catch (Exception e) {
            throw new NotUrlException(NOT_LINK_MESSAGE);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private SourceInfo tryParseSource(URI uri) throws SourceException {
        try {
            return parser.parse(uri);
        } catch (NullPointerException | SourceNotSupportedException e) {
            throw new SourceNotSupportedException("Данный ресурс не поддерживается");
        }
    }
}
