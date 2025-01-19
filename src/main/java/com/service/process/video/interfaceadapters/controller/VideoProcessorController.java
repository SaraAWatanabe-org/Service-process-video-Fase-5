package com.service.process.video.interfaceadapters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.process.video.interfaceadapters.interfaces.QueueClientAdapter;
import com.service.process.video.interfaceadapters.interfaces.StorageClientAdapter;

import com.service.process.video.usecases.FrameZip;
import com.service.process.video.usecases.Payload;


import java.io.*;
import java.nio.file.*;

public class VideoProcessorController {

    private static final String OUTPUT_DIR = "output";
    private static final String DOWNLOAD_DIR = "downloads";

    private final ObjectMapper objectMapper;

    private final QueueClientAdapter queueClientAdapter;

    private final StorageClientAdapter storageClientAdapter;

    private final FrameZip frameZip;

    public VideoProcessorController(ObjectMapper objectMapper, QueueClientAdapter queueClientAdapter, StorageClientAdapter storageClientAdapte, FrameZip frameZip) throws IOException {
        // Inicializa os diretórios locais
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        Files.createDirectories(Paths.get(DOWNLOAD_DIR));

        this.queueClientAdapter = queueClientAdapter;
        this.storageClientAdapter = storageClientAdapte;
        this.objectMapper = objectMapper;
        this.frameZip = frameZip;
    }

    public void processMessage(String message) throws IOException {


        Payload payload = objectMapper.readValue(message, Payload.class);

        System.out.println("Processing video with S3 Key: " + payload.getS3Key());

        Path path = Paths.get(DOWNLOAD_DIR, payload.getS3Key());

        // Baixa o vídeo do S3
        Path videoPath = storageClientAdapter.downloadVideoFromS3(payload.getS3Key(),path);

        // Processa o vídeo e gera o arquivo .zip
        File zipFile = frameZip.processVideo(videoPath);

        // Envia o .zip para a fila SQS
        frameZip.sendToS3AndSqs(zipFile,payload);


        System.out.println("Finished processing video: " + payload.getS3Key());
    }





}
