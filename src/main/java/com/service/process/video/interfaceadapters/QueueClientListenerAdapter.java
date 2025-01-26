package com.service.process.video.interfaceadapters;

import java.io.IOException;

public interface QueueClientListenerAdapter {

    void readMessagesFromSqs(String message) throws IOException;

}
