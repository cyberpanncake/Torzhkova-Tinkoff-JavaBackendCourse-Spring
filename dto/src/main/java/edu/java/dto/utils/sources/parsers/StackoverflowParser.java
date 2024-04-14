package edu.java.dto.utils.sources.parsers;

import edu.java.dto.utils.exception.BadSourceUrlException;
import edu.java.dto.utils.exception.SourceException;
import edu.java.dto.utils.exception.SourceNotSupportedException;
import edu.java.dto.utils.sources.info.StackoverflowInfo;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackoverflowParser extends SourceParser {
    private static final String BASE_PATTERN = "^https?://stackoverflow.com/questions";
    private static final String LINK_PATTERN = BASE_PATTERN + "/([0-9]+?)(/.*)?$";

    @Override
    StackoverflowInfo parseSource(URI uri) throws SourceException {
        String link = uri.toString();
        Pattern pattern = Pattern.compile(LINK_PATTERN);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            long questionId = Long.parseLong(matcher.group(1));
            return new StackoverflowInfo(questionId);
        }
        if (link.matches(BASE_PATTERN + ".*")) {
            throw new BadSourceUrlException("Некорретная ссылка для Stackoverflow, требуется формат\n"
                + "\"http(-s)://stackoverflow.com/{questionId}[/...]\"");
        }
        throw new SourceNotSupportedException();
    }
}
