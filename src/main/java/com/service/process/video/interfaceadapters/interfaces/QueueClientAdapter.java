package com.service.process.video.interfaceadapters.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.process.video.usecases.Payload;
import software.amazon.awssdk.services.sqs.model.Message;

import java.io.IOException;
import java.util.List;

public interface QueueClientAdapter {

    void removeMensageSqs(String message);
    void sendSQS(Payload message) throws JsonProcessingException;
    void readMessagesFromSqs(String message) throws IOException;
}
