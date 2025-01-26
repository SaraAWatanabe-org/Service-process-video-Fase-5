package com.service.process.video.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.QueueClientAdapter;
import com.service.process.video.service.model.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SendSQSClientAdapter implements QueueClientAdapter {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.endpoint-notifier}")
    private String outputQueueUrl;


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





}
