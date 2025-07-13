package com.assignment.portal26.ingestion.service;

import com.assignment.portal26.ingestion.dto.Event;
import com.assignment.portal26.ingestion.in_memory_buffer.InMemoryBuffer;
import com.assignment.portal26.ingestion.producer.Publisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventPublisher {

    Publisher publisher;

    InMemoryBuffer inMemoryBuffer;
    ObjectMapper objectMapper;

    public EventPublisher(Publisher publisher,
                          ObjectMapper objectMapper,
                          InMemoryBuffer inMemoryBuffer){
        this.publisher = publisher;
        this.objectMapper = objectMapper;
        this.inMemoryBuffer = inMemoryBuffer;
    }

    public void publishEvent(String customerId, String tier, Event event) throws  Exception {
        publisher.publish(customerId, tier, event);
    }

    public void sendEvent(Event e) throws Exception {
        String data = objectMapper.writeValueAsString(e.getIngestData());
        inMemoryBuffer.send(data);
        log.info("Data sent to in-memory buffer.");
    }



}
