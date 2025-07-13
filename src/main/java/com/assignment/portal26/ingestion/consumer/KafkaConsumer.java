package com.assignment.portal26.ingestion.consumer;

import com.assignment.portal26.ingestion.dto.Event;
import com.assignment.portal26.ingestion.dto.IngestData;
import com.assignment.portal26.ingestion.service.EventConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaConsumer  {

    EventConsumer eventConsumer;
    ObjectMapper objectMapper;
    public KafkaConsumer(EventConsumer eventConsumer, ObjectMapper objectMapper){
        this.eventConsumer = eventConsumer;
        this.objectMapper = objectMapper;
    }

//    @KafkaListener(
//            id="app-consumer-id",
//            topics = "free",
//            concurrency = "2",
//            groupId = "app-consumer-group",
//            batch = "1000",
//            containerFactory = "batchFactory")
//    void consume(List<String> list){
//        list.forEach(data -> log.info("Consumer app {}", data));
//
//    }

//    @KafkaListener(topics = "free",
//            groupId = "app-consumer-group",
//            concurrency = "3")
    public void consume(String event) throws JsonProcessingException {

//        Event event = consumerRecord.value();
//        log.info("Consumer Record Key {}, Consumer Record Value {}",
//                consumerRecord.key(), event);
        log.info("Consumer Record {} ", event);
//        Event event1 = objectMapper.readValue(objectMapper.readTree(event).asText(), Event.class);
        Event event1 = objectMapper.readValue(event, Event.class);
        log.info("Event {}", event1);
        try {
            eventConsumer.consume(List.of(event1));
        } catch (Exception e){
            // failed to process msg
            e.printStackTrace();
        }
    }



}
