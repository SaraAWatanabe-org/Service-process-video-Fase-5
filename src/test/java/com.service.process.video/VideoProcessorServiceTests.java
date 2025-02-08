package com.service.process.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.QueueClientAdapter;
import com.service.process.video.interfaceadapters.StorageClientAdapter;
import com.service.process.video.service.FrameZip;
import com.service.process.video.service.VideoProcessorService;
import com.service.process.video.service.enums.NotificationType;
import com.service.process.video.service.model.Notification;
import com.service.process.video.service.model.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestAwsConfig.class)
class VideoProcessorServiceTests {

    @Mock
    private QueueClientAdapter queueClientAdapter;

    @Mock
    private StorageClientAdapter storageClientAdapter;

    @Mock
    private FrameZip frameZip;

    @Mock
    private Logger log;

    @InjectMocks
    private VideoProcessorService videoProcessorService;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Files.createDirectories(Paths.get("output"));
        Files.createDirectories(Paths.get("downloads"));
    }

    @Test
    void processMessageSuccessfully() throws IOException {
        String message = "{\"key\":\"video.mp4\",\"email\":\"test@example.com\",\"id\":\"65e529f7-cf50-4682-8d32-fc5e4b5cb3e4\"}";
        Payload payload = new ObjectMapper().readValue(message, Payload.class);
        Path videoPath = Paths.get("downloads", payload.getKey());
        File zipFile = new File("output/video.zip");

        when(storageClientAdapter.downloadVideoFromS3(payload.getKey(), videoPath)).thenReturn(videoPath);
        when(frameZip.processVideo(videoPath)).thenReturn(zipFile);
        when(storageClientAdapter.uploadVideoFromS3(zipFile, payload)).thenReturn(payload);

        videoProcessorService.processMessage(message);

        assertThat(new File("output").listFiles()).isEmpty();
    }

    @Test
    void processMessageWithException() throws IOException {
        String message = "{\"key\":\"video.mp4\",\"email\":\"test@example.com\",\"id\":\"99b4d713-4f75-4cc3-a43a-8b9608139f48\"}";
        Payload payload = new ObjectMapper().readValue(message, Payload.class);
        Path videoPath = Paths.get("downloads", payload.getKey());

        when(storageClientAdapter.downloadVideoFromS3(payload.getKey(), videoPath)).thenThrow(new IOException("Download error"));

        try {
            videoProcessorService.processMessage(message);
        } catch (Exception e) {
            verify(queueClientAdapter).sendSQS(any(Notification.class));
            verify(log).error(anyString(), eq(payload.getKey()), any(Exception.class));
        }
    }

    @Test
    void getNotificationReturnsCorrectNotification() {
        Payload payload = new Payload();
        payload.setEmail("test@example.com");
        payload.setKey("video.mp4");
        payload.setId(UUID.fromString("99b4d713-4f75-4cc3-a43a-8b9608139f48"));

        Notification notification = VideoProcessorService.getNotification(payload, NotificationType.SUCCESS);

        assertThat(notification.getEmail()).isEqualTo("test@example.com");
        assertThat(notification.getBucketAddress()).isEqualTo("video.mp4");
        assertThat(notification.getVideoId()).isEqualTo(UUID.fromString("99b4d713-4f75-4cc3-a43a-8b9608139f48"));
        assertThat(notification.getNotificationType()).isEqualTo(NotificationType.SUCCESS);
    }
}