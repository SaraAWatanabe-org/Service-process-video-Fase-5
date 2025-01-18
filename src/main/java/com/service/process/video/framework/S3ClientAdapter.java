package com.service.process.video.framework;

import com.service.process.video.interfaceadapters.interfaces.StorageClientAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class S3ClientAdapter implements StorageClientAdapter {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public S3ClientAdapter() throws IOException {

        // Configura o cliente S3
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

    }


    public Path downloadVideoFromS3(String s3Key,Path videoPath) throws IOException {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        try (InputStream inputStream = s3Client.getObject(getObjectRequest);
             OutputStream outputStream = Files.newOutputStream(videoPath)) {
            inputStream.transferTo(outputStream);
        }

        return videoPath;
    }
}
