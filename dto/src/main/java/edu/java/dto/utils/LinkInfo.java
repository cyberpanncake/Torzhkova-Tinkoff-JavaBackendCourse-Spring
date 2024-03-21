package edu.java.dto.utils;

import edu.java.dto.utils.sources.info.SourceInfo;
import java.net.URI;

public record LinkInfo(URI url, SourceInfo source) {
}
