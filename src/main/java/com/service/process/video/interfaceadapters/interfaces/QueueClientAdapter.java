package com.service.process.video.interfaceadapters.interfaces;

import software.amazon.awssdk.services.sqs.model.Message;

import java.io.IOException;
import java.util.List;

public interface QueueClientAdapter {

    void removeMensageSqs(String message);
    void sendSQS(String message);
    void readMessagesFromSqs(String message) throws IOException;
}
