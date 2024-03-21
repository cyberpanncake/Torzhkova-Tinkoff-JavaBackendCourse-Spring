package edu.java.dto.utils.sources.parsers;

import edu.java.dto.utils.exception.SourceNotSupportedException;
import edu.java.dto.utils.sources.info.SourceInfo;
import java.net.URI;
import java.util.Set;

public abstract class SourceParser {
    protected SourceParser next;

    public void setNext(SourceParser next) {
        this.next = next;
    }

    public SourceInfo parse(URI url) throws SourceNotSupportedException {
        try {
            return parseSource(url);
        } catch (SourceNotSupportedException e) {
            if (next != null) {
                return parse(url);
            }
            throw e;
        }
    }

    abstract SourceInfo parseSource(URI url) throws SourceNotSupportedException;

    public static SourceParser buildChain(Set<SourceParser> parsers) {
        SourceParser first = null;
        for (SourceParser parser : parsers) {
            parser.setNext(first);
            first = parser;
        }
        return first;
    }
}
