package edu.java.scrapper.api.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(long id, URI url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
}
