package com.assignment.portal26.ingestion.service;

import com.assignment.portal26.ingestion.dto.Event;
import com.assignment.portal26.ingestion.dto.IngestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventConsumer {

    ObjectMapper objectMapper;
    S3Service s3Service;


    public EventConsumer(ObjectMapper objectMapper, S3Service s3Service){
        this.objectMapper = objectMapper;
        this.s3Service = s3Service;


    }

    public void consume(List<Event> events) throws Exception {

        Map<String, List<IngestData>> customerIdVsEvents = events.stream()
                .collect(Collectors.groupingBy(Event::getCustomerId,
                        Collectors.mapping(Event::getIngestData, Collectors.toList())));

        String data = objectMapper.writeValueAsString(customerIdVsEvents.values());

        for(Map.Entry<String, List<IngestData>> entry : customerIdVsEvents.entrySet()) {
            String path = entry.getKey();
            s3Service.upload(path, data);
        }
    }

//    public static void main(String[] args) throws Exception {
//        String data = "This is test payload";
//        IngestData id = new IngestData(OffsetDateTime.now(), data);
//        System.out.println(id.getEventTimestamp());
//        List<Event> events = List.of(
//                new Event(Tier.FREE, "abc", LocalDateTime.now(), data.length(), id),
//                new Event(Tier.FREE, "abc", LocalDateTime.now(), data.length(), id),
//                new Event(Tier.FREE, "abc", LocalDateTime.now(), data.length(), id)
//        );
//        new EventConsumer(new ObjectMapper(), null).consume(events);
//    }



}
