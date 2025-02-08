package com.service.process.video.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.QueueClientAdapter;
import com.service.process.video.service.model.Notification;
import com.service.process.video.service.model.Payload;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SendSQSClientAdapter implements QueueClientAdapter {
    Logger log = LoggerFactory.getLogger(SendSQSClientAdapter.class);

    private final SqsClient sqsClient;

    @Value("${aws.sqs.endpoint-notifier}")
    private String outputQueueUrl;


    public void sendSQS(Notification payload) throws JsonProcessingException {
        try {
            // Envia a mensagem para a fila SQS
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(payload);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(outputQueueUrl)
                    .messageBody(json)
                    .build();

            sqsClient.sendMessage(request);
            log.info("Message sent successfully to SQS with payload: {}", json);
        } catch (Exception e) {
            log.error("Error sending message to SQS with payload: {}", payload, e);
            throw e;
        }
    }





}
