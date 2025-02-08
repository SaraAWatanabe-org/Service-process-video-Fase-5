package com.service.process.video.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.QueueClientAdapter;
import com.service.process.video.interfaceadapters.StorageClientAdapter;
import com.service.process.video.service.enums.NotificationType;
import com.service.process.video.service.model.Notification;
import com.service.process.video.service.model.Payload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.io.*;
import java.nio.file.*;

@Component
public class VideoProcessorService {

    private static final String OUTPUT_DIR = "output";
    private static final String DOWNLOAD_DIR = "downloads";
    Logger log = LoggerFactory.getLogger(VideoProcessorService.class);


    private final QueueClientAdapter queueClientAdapter;

    private final StorageClientAdapter storageClientAdapter;

    private final FrameZip frameZip;

    public VideoProcessorService(QueueClientAdapter queueClientAdapter, StorageClientAdapter storageClientAdapte, FrameZip frameZip) throws IOException {
        // Inicializa os diretórios locais
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        Files.createDirectories(Paths.get(DOWNLOAD_DIR));

        this.queueClientAdapter = queueClientAdapter;
        this.storageClientAdapter = storageClientAdapte;
        this.frameZip = frameZip;
    }

    public void processMessage(String message) throws IOException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Payload payload = objectMapper.readValue(message, Payload.class);

            log.info("Processing video with S3 Key: {}", payload.getKey());

            Path path = Paths.get(DOWNLOAD_DIR, payload.getKey());

            // Baixa o vídeo do S3
            Path videoPath = storageClientAdapter.downloadVideoFromS3(payload.getKey(), path);

            // Processa o vídeo e gera o arquivo .zip
            File zipFile = frameZip.processVideo(videoPath);

            // Envia o .zip para o S3
            Payload payloadS3Zip = storageClientAdapter.uploadVideoFromS3(zipFile, payload);

            Notification notification = getNotification(payload, NotificationType.SUCCESS);

            // Envia o payload para a fila SQS
            queueClientAdapter.sendSQS(notification);

            // Limpeza dos arquivos temporários (frames)
            FileUtils.cleanDirectory(new File(OUTPUT_DIR));

            log.info("Finished processing video: {}", payload.getKey());
        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            Payload payload = objectMapper.readValue(message, Payload.class);

            Notification notification = getNotification(payload,  NotificationType.ERROR);

            // Envia o payload para a fila SQS
            queueClientAdapter.sendSQS(notification);

            log.error("Error processing video with S3 Key: {}", payload.getKey(), e);
            throw e;
        }
    }

    public static Notification getNotification(Payload payload, NotificationType notificationType) {
        Notification notification = new Notification();
        notification.setEmail(payload.getEmail());
        notification.setBucketAddress(payload.getKey());
        notification.setVideoId(payload.getId());
        notification.setNotificationType(notificationType);
        return notification;
    }


}
