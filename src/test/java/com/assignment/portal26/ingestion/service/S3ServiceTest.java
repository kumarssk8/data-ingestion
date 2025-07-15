package com.assignment.portal26.ingestion.service;

import com.assignment.portal26.ingestion.metrics.CounterMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.S3Client;

public class S3ServiceTest {

    private S3Client s3Client;
    private CounterMetrics counterMetrics;
    private S3Service s3Service;

    private final String ipAddress = "192.168.0.1";
    private final String bucketName = "test_bucket";
    @BeforeEach
    void setup(){
        s3Client = Mockito.mock(S3Client.class);
        counterMetrics = Mockito.mock(CounterMetrics.class);
        s3Service = new S3Service(s3Client, counterMetrics, ipAddress, bucketName);
    }




}
