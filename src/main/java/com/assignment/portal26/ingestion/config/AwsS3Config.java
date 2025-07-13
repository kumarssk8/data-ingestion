package com.assignment.portal26.ingestion.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Component
public class AwsS3Config {

    public String accessKey;
    public String secretKey;

    public AwsS3Config(@Value("${app.aws.access.key}") String accessKey,
                       @Value("${app.aws.secret.key}") String secretKey){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Bean
    public S3Client s3Client(){
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.US_EAST_1);

        if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
            AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            );
            builder.credentialsProvider(credentialsProvider);
        } else {
            // In prod, it will be from IAM
            builder.credentialsProvider(DefaultCredentialsProvider.builder().build());
        }
        return  builder.build();
    }



}
