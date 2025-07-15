package com.assignment.portal26.ingestion.controller;

import com.assignment.portal26.ingestion.dto.IngestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IngestionControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    public void testIngestionController() throws Exception {

        IngestData ingestData = new IngestData();
        ingestData.setEventTimestamp(OffsetDateTime.now());
        ingestData.setBody("This is a testing content");

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/ingest")
                .header("X-Customer-Tier","pro")
                .contentType("application/json")
                .content(asString(ingestData))
                .accept("application/json");


        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }

    @Test
    public void testCustomTierHttpHeader() throws Exception {
        IngestData ingestData = new IngestData();
        ingestData.setEventTimestamp(OffsetDateTime.now());
        ingestData.setBody("This is a testing content");

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/ingest")
                //.header("X-Customer-Tier","pro")
                .contentType("application/json")
                .content(asString(ingestData))
                .accept("application/json");

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attribute")
                        .value("X-Customer-Tier"));

    }

    @Test
    public void testLargePayload() throws Exception {
        IngestData ingestData = new IngestData();
        ingestData.setEventTimestamp(OffsetDateTime.now());
        ingestData.setBody("a".repeat(1024 * 1024 * 5 + 1));

        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/ingest")
                .header("X-Customer-Tier","pro")
                .contentType("application/json")
                .content(asString(ingestData))
                .accept("application/json");

        mockMvc.perform(req)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.attribute")
                        .value("body"));

    }


    private String asString(IngestData ingestData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(ingestData);
    }
}
