package com.assignment.portal26.ingestion.in_memory_buffer;

import com.assignment.portal26.ingestion.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.*;

import static java.util.concurrent.Executors.newScheduledThreadPool;

@Service
@Slf4j
public class InMemoryBuffer {

    final int maxSize;
    final int maxSecs;

    volatile int size = 0;
    volatile StringBuilder sb = new StringBuilder();
    volatile long lastCheckedTime = System.currentTimeMillis();

    final Object lock = new Object();

    S3Service s3Service;

    public InMemoryBuffer(S3Service s3Service,
                          @Value("${app.buffer.max_size.mb}") int maxSizeInMb,
                          @Value("${app.buffer.max_time.secs}") int maxSecWait){
        this.s3Service = s3Service;
        this.maxSize = maxSizeInMb * 1024 * 1024;
        this.maxSecs = maxSecWait;
    }

    @Async("virtualThreadExecutor")
    public void send(String s){
        synchronized (lock) {
            if(!sb.isEmpty()){
                sb.append(",");
            }
            sb.append(s);
            size += (s.length() * 2); // utf-16 uses 2 bytes for each character in memory
            if (size > maxSize) {
                log.info("Performing memory flush");
                uploadAndReset();
            }
        }
    }

    public void timeFlush(){
        synchronized (lock){
            long currentTime = System.currentTimeMillis();
            long seconds = ((currentTime - lastCheckedTime) / 1000) ;
            if(seconds >= maxSecs && !sb.isEmpty()){
                log.info("Performing time flush");
                uploadAndReset();
            }
            lastCheckedTime = System.currentTimeMillis();
        }
    }

    private void uploadAndReset(){
        synchronized (lock) {
            s3Service.upload("", sb.toString());
            sb = new StringBuilder();
            size = 0;
        }

    }



}
