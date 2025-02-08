package com.service.process.video.framework;

import com.service.process.video.interfaceadapters.QueueClientListenerAdapter;
import com.service.process.video.service.VideoProcessorService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ListenerSQSClientAdapter implements QueueClientListenerAdapter {

    Logger log = LoggerFactory.getLogger(ListenerSQSClientAdapter.class);

    private final VideoProcessorService videoProcessorController;


    @SqsListener("${aws.sqs.endpoint}")
    public void readMessagesFromSqs(String message) throws IOException {
        log.info("Message received from queue: {}", message);
        videoProcessorController.processMessage(message);
    }

}
