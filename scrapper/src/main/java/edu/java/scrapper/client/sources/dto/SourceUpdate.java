package edu.java.scrapper.client.sources.dto;

import java.time.OffsetDateTime;

public interface SourceUpdate {

    String getDetailsDescription();

    OffsetDateTime getDate();
}
