package com.service.process.video.framework;

import com.service.process.video.interfaceadapters.StorageClientAdapter;
import com.service.process.video.service.model.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class S3ClientAdapter implements StorageClientAdapter {

    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;


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

    public Payload uploadVideoFromS3(File file,Payload payload) throws IOException {
        String key = buildS3Key("exampleUser@example.com", file.getName());

        // Upload para o S3
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                file.toPath());

//        s3Client.putObject(PutObjectRequest.builder()
//                        .bucket(bucketName)
//                        .key(key)
//                        .build(),
//                RequestBody.fromFile(file));

        // Construir a URL de acesso do objeto S3
        String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toExternalForm();
        payload.setUrl(fileUrl);
        payload.setS3Key(key);

        return payload;
    }


    private String buildS3Key(String userName, String originalFilename) {
        LocalDate now = LocalDate.now();
        return String.format("%s/%d/%02d/%02d/%s", userName, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), originalFilename);
    }
}
