package com.service.process.video.framework;

import com.service.process.video.interfaceadapters.QueueClientListenerAdapter;
import com.service.process.video.service.VideoProcessorService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ListenerSQSClientAdapter implements QueueClientListenerAdapter {


    private final VideoProcessorService videoProcessorController;


    @SqsListener("${aws.sqs.endpoint}")
    public void readMessagesFromSqs(String message) throws IOException {
        System.out.println("Mensagem recebida da fila: " + message);
        videoProcessorController.processMessage(message);
    }

}
