package com.service.process.video;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestAwsConfig {

    @Bean
    @Primary
    public SqsAsyncClient sqsAsyncClient() {
        return mock(SqsAsyncClient.class);
    }
}