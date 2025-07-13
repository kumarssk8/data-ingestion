package com.assignment.portal26.ingestion.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CounterMetrics {

    MeterRegistry meterRegistry;
    Counter s3UploadCounter;
    public CounterMetrics(MeterRegistry meterRegistry){
        s3UploadCounter = meterRegistry.counter("s3.upload");
    }

    public void logS3UploadMetric(){
        s3UploadCounter.increment();
    }


}
