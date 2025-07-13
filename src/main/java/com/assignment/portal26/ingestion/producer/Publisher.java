package com.assignment.portal26.ingestion.producer;


import com.assignment.portal26.ingestion.dto.Event;

public interface Publisher {
    void publish(String customerId, String tier, Event event) throws Exception;
}
