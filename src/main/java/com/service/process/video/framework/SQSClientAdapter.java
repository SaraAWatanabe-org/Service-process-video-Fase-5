package com.service.process.video.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.controller.VideoProcessorController;
import com.service.process.video.interfaceadapters.interfaces.QueueClientAdapter;
import com.service.process.video.usecases.Payload;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SQSClientAdapter implements QueueClientAdapter {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.input-queue-url}")
    private String inputQueueUrl;

    @Value("${aws.sqs.output-queue-url}")
    private String outputQueueUrl;

    private VideoProcessorController videoProcessorController;


    public SQSClientAdapter() throws IOException {

        // Configura o cliente SQS
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @SqsListener("nome-da-fila-sqs")
    public void readMessagesFromSqs(String message) throws IOException {
        videoProcessorController.processMessage(message);
    }

    public void sendSQS(Payload payload) throws JsonProcessingException {
        // Envia a mensagem para a fila SQS

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(payload);

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(outputQueueUrl)
                .messageBody(json)
                .build();

        sqsClient.sendMessage(request);
    }

    public void removeMensageSqs(String message) {
        // Remove a mensagem da fila
        sqsClient.deleteMessage(b -> b.queueUrl(inputQueueUrl).receiptHandle(message));
    }



}
