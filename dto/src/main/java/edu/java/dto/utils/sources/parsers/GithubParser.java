package edu.java.dto.utils.sources.parsers;

import edu.java.dto.utils.exception.BadSourceUrlException;
import edu.java.dto.utils.exception.SourceException;
import edu.java.dto.utils.exception.SourceNotSupportedException;
import edu.java.dto.utils.sources.info.GithubInfo;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubParser extends SourceParser {
    private static final String BASE_PATTERN = "^https?://github.com";
    private static final String LINK_PATTERN = BASE_PATTERN + "/(.+?)/(.+?)(/.*)?$";

    @Override
    GithubInfo parseSource(URI uri) throws SourceException {
        String link = uri.toString();
        Pattern pattern = Pattern.compile(LINK_PATTERN);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            String owner = matcher.group(1);
            String repo = matcher.group(2);
            return new GithubInfo(owner, repo);
        }
        if (link.matches(BASE_PATTERN + ".*")) {
            throw new BadSourceUrlException("Некорретная ссылка для GitHub, требуется формат\n"
                + "\"http(-s)://github.com/{owner}/{repo}[/...]\"");
        }
        throw new SourceNotSupportedException();
    }
}
