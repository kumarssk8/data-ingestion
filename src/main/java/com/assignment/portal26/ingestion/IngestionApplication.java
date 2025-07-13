package com.assignment.portal26.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class IngestionApplication {

	public static void main(String[] args) {

		SpringApplication.run(IngestionApplication.class, args);

	}

}
