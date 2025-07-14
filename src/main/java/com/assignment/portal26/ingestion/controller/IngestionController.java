package com.assignment.portal26.ingestion.controller;

import com.assignment.portal26.ingestion.dto.IngestData;
import com.assignment.portal26.ingestion.dto.IngestResponse;
import com.assignment.portal26.ingestion.exception.InvalidHeaderException;
import com.assignment.portal26.ingestion.exception.InvalidRequestException;
import com.assignment.portal26.ingestion.service.InMemoryAccumulatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;


@RestController
@Slf4j
public class IngestionController {

    String[] tiers;
    int payloadSize;
    InMemoryAccumulatorService inMemoryAccumulatorService;

    public IngestionController(
                               @Qualifier("tiers") String[] tiers,
                               @Value("${app.req.payload.size.mb}") Integer payloadSize,
                               InMemoryAccumulatorService inMemoryAccumulatorService){
        this.tiers = tiers;
        this.payloadSize = payloadSize * 1024 * 1024;
        this.inMemoryAccumulatorService = inMemoryAccumulatorService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<IngestResponse> ingest(
            @RequestHeader(value = "X-Customer-Tier", required = false) String tier,
            @RequestHeader(value = "X-Customer-Id", required = false) String customerId,
            HttpServletRequest httpServletRequest,
            @RequestBody IngestData ingestReq) throws Exception {
        if(Strings.isBlank(tier)){
            throw new InvalidHeaderException("X-Customer-Tier", "Header value is missing");
        }
        Optional<String> customerTier = Arrays.stream(tiers).filter(t -> t.equalsIgnoreCase(tier)).findFirst();
        if(customerTier.isEmpty()){
            throw new InvalidHeaderException("X-Customer-Tier", "Invalid value");
        }
//        if(Strings.isBlank(customerId)){
//            throw new InvalidHeaderException("X-Customer-Id", "Invalid value");
//        }
        if(httpServletRequest.getContentLength() > payloadSize){
            throw new InvalidRequestException("Body size is too big to process", "body");
        }

        inMemoryAccumulatorService.push(ingestReq);
        return ResponseEntity.ok(new IngestResponse("success"));
    }

}
