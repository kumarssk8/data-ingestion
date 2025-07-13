package com.assignment.portal26.ingestion.service;

import com.assignment.portal26.ingestion.metrics.CounterMetrics;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class S3Service {


    S3Client s3Client;
    String bucketName;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    CounterMetrics counterMetrics;
    String ipAddress;

    public S3Service(S3Client s3Client,
                     CounterMetrics counterMetrics,
                     @Qualifier("ipAddress") String ipAddress,
                     @Value("${app.aws.bucket}") String bucketName){
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.counterMetrics = counterMetrics;
        this.ipAddress = ipAddress;
    }

    @Async("asyncThreadExecutor")
    public void upload(String prefix, String data) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String key = prefix + dateFormatter.format(localDateTime)
                   + "/" + ipAddress + ":" + timeFormatter.format(localDateTime);
            log.info("File uploading with key {}", key);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("text/plain")
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromString(data));
            counterMetrics.logS3UploadMetric();
    }

}
