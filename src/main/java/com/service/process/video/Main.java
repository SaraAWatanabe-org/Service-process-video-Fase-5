package com.service.process.video;

import com.service.process.video.framework.S3ClientAdapter;
import com.service.process.video.framework.SQSClientAdapter;
import com.service.process.video.interfaceadapters.QueueClientAdapter;
import com.service.process.video.interfaceadapters.StorageClientAdapter;
import com.service.process.video.service.FrameZip;
import com.service.process.video.service.VideoProcessorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.service.process.video.config.AwsConfig;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {

            FrameZip frameZip = new FrameZip();
            Path videoPath = Paths.get("downloads/videoexemplo.mp4");
            frameZip.processVideo(videoPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}