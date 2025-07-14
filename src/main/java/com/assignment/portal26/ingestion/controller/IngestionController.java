package com.assignment.portal26.ingestion.controller;

import com.assignment.portal26.ingestion.dto.Event;
import com.assignment.portal26.ingestion.dto.IngestData;
import com.assignment.portal26.ingestion.dto.IngestResponse;
import com.assignment.portal26.ingestion.exception.InvalidHeaderException;
import com.assignment.portal26.ingestion.service.EventPublisher;
import com.assignment.portal26.ingestion.service.InMemoryAccumulatorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;


@RestController
@Slf4j
public class IngestionController {


    EventPublisher eventPublisher;
    String[] tiers;
    InMemoryAccumulatorService inMemoryAccumulatorService;

    public IngestionController(EventPublisher eventPublisher,
                               @Qualifier("tiers") String[] tiers,
                               InMemoryAccumulatorService inMemoryAccumulatorService){
        this.eventPublisher = eventPublisher;
        this.tiers = tiers;
        this.inMemoryAccumulatorService = inMemoryAccumulatorService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<IngestResponse> ingest(
            @RequestHeader(value = "X-Customer-Tier", required = false) String tier,
            @RequestHeader(value = "X-Customer-Id", required = false) String customerId,
            @RequestBody IngestData ingestReq) throws Exception {
        if(Strings.isBlank(tier)){
            throw new InvalidHeaderException("X-Customer-Tier", "Header value is missing");
        }
        Optional<String> customerTier = Arrays.stream(tiers).filter(t -> t.equalsIgnoreCase(tier)).findFirst();
        if(customerTier.isEmpty()){
            throw new InvalidHeaderException("X-Customer-Tier", "Invalid value");
        }
        if(Strings.isBlank(customerId)){
            throw new InvalidHeaderException("X-Customer-Id", "Invalid value");
        }

//        Event event = new Event(customerTier.get(), customerId, LocalDateTime.now(), ingestReq.toString().length(), ingestReq);
//        eventPublisher.publishEvent(customerId, enumTier, event);
        inMemoryAccumulatorService.push(ingestReq);
        return ResponseEntity.ok(new IngestResponse("success"));
    }

}
