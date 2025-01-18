package com.service.process.video.framework;

import com.service.process.video.interfaceadapters.controller.VideoProcessorController;
import com.service.process.video.interfaceadapters.interfaces.QueueClientAdapter;
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

    public void sendSQS(String message) {
        // Envia a mensagem para a fila SQS
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(outputQueueUrl)
                .messageBody(message)
                .build();

        sqsClient.sendMessage(request);
    }

    public void removeMensageSqs(String message) {
        // Remove a mensagem da fila
        sqsClient.deleteMessage(b -> b.queueUrl(inputQueueUrl).receiptHandle(message));
    }



}
