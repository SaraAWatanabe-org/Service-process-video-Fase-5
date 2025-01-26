package com.service.process.video;

import com.service.process.video.framework.S3ClientAdapter;
import com.service.process.video.service.FrameZip;
import com.service.process.video.service.VideoProcessorService;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {

            FrameZip frameZip = new FrameZip();

            S3Client build = null;

            S3ClientAdapter s3ClientAdapter = new S3ClientAdapter(build);
            VideoProcessorService videoProcessorService = new VideoProcessorService(null, s3ClientAdapter, frameZip);
//            Path videoPath = Paths.get("downloads/videoexemplo.mp4");
//            frameZip.processVideo(videoPath);
            videoProcessorService.processMessage("{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"email\":\"examp6leUser@example.com\",\"url\":\"https://soat-storage.s3.sa-east-1.amazonaws.com/exampleUser2%40example.com/2025/01/25/teste0video0s3.mp4\",\"s3Key\":\"exampleUser2@example.com-2025-01-25-teste0video0s3.mp4\"}");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}