package edu.java.scrapper.api.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(Long id, URI url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
}
