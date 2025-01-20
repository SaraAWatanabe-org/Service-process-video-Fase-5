package com.service.process.video.interfaceadapters;

import com.service.process.video.service.model.Payload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageClientAdapter {
    Payload uploadVideoFromS3(File file,Payload payload) throws IOException;
    Path downloadVideoFromS3(String s3Key,Path videoPath ) throws IOException;
}
