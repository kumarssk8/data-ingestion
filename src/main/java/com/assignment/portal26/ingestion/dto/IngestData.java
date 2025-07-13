package com.assignment.portal26.ingestion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class IngestData {
    @JsonProperty("event_timestamp")
    OffsetDateTime eventTimestamp;
    String body;
}
