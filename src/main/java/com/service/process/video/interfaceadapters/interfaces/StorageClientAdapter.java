package com.service.process.video.interfaceadapters.interfaces;

import com.service.process.video.usecases.Payload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageClientAdapter {
    Payload uploadVideoFromS3(File file,Payload payload) throws IOException;
    Path downloadVideoFromS3(String s3Key,Path videoPath ) throws IOException;
}
