package com.assignment.portal26.ingestion.service;

import com.assignment.portal26.ingestion.dto.IngestData;
import com.assignment.portal26.ingestion.in_memory_buffer.InMemoryBuffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

@Service
@Slf4j
public class InMemoryAccumulatorService {

    InMemoryBuffer inMemoryBuffer;
    ObjectMapper objectMapper;

    public InMemoryAccumulatorService(InMemoryBuffer inMemoryBuffer,
                                      ObjectMapper objectMapper,
                                      @Value("${app.buffer.time_flush.interval.secs}") long timeflushInSecs) {
        this.inMemoryBuffer = inMemoryBuffer;
        this.objectMapper = objectMapper;
        ScheduledExecutorService scheduler = newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(inMemoryBuffer::timeFlush, 0, timeflushInSecs, TimeUnit.SECONDS);
    }

    public void push(IngestData ingestData) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(ingestData);
        inMemoryBuffer.send(data);
        log.info("Data sent to in-memory buffer.");
    }
}
