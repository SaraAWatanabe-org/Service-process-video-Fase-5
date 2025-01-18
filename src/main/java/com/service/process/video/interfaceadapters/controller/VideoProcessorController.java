package com.service.process.video.interfaceadapters.controller;

import com.service.process.video.interfaceadapters.interfaces.QueueClientAdapter;
import com.service.process.video.interfaceadapters.interfaces.StorageClientAdapter;

import com.service.process.video.usecases.FrameZip;


import java.io.*;
import java.nio.file.*;

public class VideoProcessorController {

    private static final String OUTPUT_DIR = "output";
    private static final String DOWNLOAD_DIR = "downloads";

    private final QueueClientAdapter queueClientAdapter;

    private final StorageClientAdapter storageClientAdapter;

    private final FrameZip frameZip;

    public VideoProcessorController(QueueClientAdapter queueClientAdapter, StorageClientAdapter storageClientAdapte, FrameZip frameZip) throws IOException {
        // Inicializa os diretórios locais
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        Files.createDirectories(Paths.get(DOWNLOAD_DIR));

        this.queueClientAdapter = queueClientAdapter;
        this.storageClientAdapter = storageClientAdapte;
        this.frameZip = frameZip;
    }

    public void processMessage(String message) throws IOException {

        //TODO: precisa ver como vai pegar a key da mensagem
        String s3Key = message;

        System.out.println("Processing video with S3 Key: " + s3Key);

        Path path = Paths.get(DOWNLOAD_DIR, s3Key);

        // Baixa o vídeo do S3
        Path videoPath = storageClientAdapter.downloadVideoFromS3(s3Key,path);

        // Processa o vídeo e gera o arquivo .zip
        File zipFile = frameZip.processVideo(videoPath);

        // Envia o .zip para a fila SQS
        frameZip.sendZipToSqs(zipFile);

        queueClientAdapter.removeMensageSqs(message);

        System.out.println("Finished processing video: " + s3Key);
    }





}
