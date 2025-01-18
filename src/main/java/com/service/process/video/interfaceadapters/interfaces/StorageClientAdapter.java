package com.service.process.video.interfaceadapters.interfaces;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageClientAdapter {

    Path downloadVideoFromS3(String s3Key,Path videoPath ) throws IOException;
}
