package com.service.process.video.interfaceadapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.process.video.service.model.Payload;

import java.io.IOException;

public interface QueueClientAdapter {

    void removeMensageSqs(String message);
    void sendSQS(Payload message) throws JsonProcessingException;
    void readMessagesFromSqs(String message) throws IOException;
}
