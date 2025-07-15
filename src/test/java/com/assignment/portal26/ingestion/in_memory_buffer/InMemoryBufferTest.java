package com.assignment.portal26.ingestion.in_memory_buffer;


import com.assignment.portal26.ingestion.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import static org.mockito.Mockito.*;

@SpringBootTest
public class InMemoryBufferTest {

    private S3Service s3Service;
    private InMemoryBuffer inMemoryBuffer;

    private final int maxSizeMb = 1;
    private final int maxTimeSecs = 2;

    @BeforeEach
    void setup(){
        s3Service = mock(S3Service.class);
        inMemoryBuffer = new InMemoryBuffer(s3Service, maxSizeMb, maxTimeSecs);
    }

    @Test
    void testSendShouldNotFlushIfBelowThreshold(){
        String testData = "abc";
        inMemoryBuffer.send(testData);
        verify(s3Service, never()).upload(anyString(), anyString());
    }

    @Test
    void testShouldFlushWhenMemoryLimitExceeded(){
        String largeData = "a".repeat(1024 * 1024 + 1);
        inMemoryBuffer.send(largeData);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
        verify(s3Service, times(1))
                .upload(keyCaptor.capture(), dataCaptor.capture());

        AssertionErrors.assertEquals("key should be null", "", keyCaptor.getValue());
        AssertionErrors.assertEquals("value is not same", largeData, dataCaptor.getValue());

    }

    @Test
    void testStringAppended(){
        String data1 = "a".repeat(1024 );
        inMemoryBuffer.send(data1);
        verify(s3Service, never())
                .upload(any(), any());
        String data2 = "b".repeat(1024 * 1024);
        inMemoryBuffer.send(data2);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
        verify(s3Service, times(1))
                .upload(keyCaptor.capture(), dataCaptor.capture());

        AssertionErrors.assertEquals("Key should be empty", "", keyCaptor.getValue());
        AssertionErrors.assertEquals("Value should be same", data1 +","+ data2, dataCaptor.getValue());
    }

    @Test
    void testTimeFlush() throws InterruptedException {
        String data1 = "a".repeat(1024 );
        inMemoryBuffer.send(data1);
        inMemoryBuffer.timeFlush();
        verify(s3Service, never())
                .upload(any(), any());
        Thread.sleep(2000);
        inMemoryBuffer.timeFlush();
        verify(s3Service, times(1))
                .upload(any(), any());
    }
}
