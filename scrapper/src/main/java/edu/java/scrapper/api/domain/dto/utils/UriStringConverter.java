package edu.java.scrapper.api.domain.dto.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;
import org.springframework.util.StringUtils;

@Converter
public class UriStringConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return (uri == null) ? null : uri.toString();
    }

    @Override
    public URI convertToEntityAttribute(String s) {
        return (StringUtils.hasLength(s) ? URI.create(s.trim()) : null);
    }
}
