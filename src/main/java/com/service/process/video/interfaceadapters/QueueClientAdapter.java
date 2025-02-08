package com.service.process.video.interfaceadapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.process.video.service.model.Notification;
import com.service.process.video.service.model.Payload;

import java.io.IOException;

public interface QueueClientAdapter {

    void sendSQS(Notification message) throws JsonProcessingException;
}
