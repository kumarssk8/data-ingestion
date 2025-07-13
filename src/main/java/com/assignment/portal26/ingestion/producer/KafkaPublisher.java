package com.assignment.portal26.ingestion.producer;

import com.assignment.portal26.ingestion.dto.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPublisher implements Publisher {

    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;

    public KafkaPublisher(KafkaTemplate<String,String> kafkaTemplate,
                          ObjectMapper objectMapper){
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(String customerId, String tier, Event event) throws Exception {
        String topic = topic(customerId, tier);
        Integer partition = partition(customerId);
        kafkaTemplate.send(topic, partition,  null, objectMapper.writeValueAsString(event));

    }

    public String topic(String customerId, String tier){
        // we can store customer specific topic as configuration in db,
        // if not we use tier itself as the topic name
        return tier;
    }

    public Integer partition(String customerId){
        return null;
    }

}
