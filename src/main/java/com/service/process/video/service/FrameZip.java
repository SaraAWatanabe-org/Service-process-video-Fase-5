package com.service.process.video.service;


import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FrameZip {

    private static final String OUTPUT_DIR = "output";
    private static final String DOWNLOAD_DIR = "downloads";


    public FrameZip() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        Files.createDirectories(Paths.get(DOWNLOAD_DIR));
    }


    static {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    public File processVideo(Path videoPath) throws IOException {
        // Caminhos para os executáveis do FFmpeg e FFprobe
        FFmpeg ffmpeg = new FFmpeg("/path/to/ffmpeg");
        FFprobe ffprobe = new FFprobe("/path/to/ffprobe");

        // Configura FFmpeg para extrair frames
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath.toString())  // Define o vídeo de entrada
                .addOutput(OUTPUT_DIR + "/frame%03d.png")  // Saída dos frames (ex: frame001.png)
                .setFormat("image2")  // Formato de imagem
                .setVideoCodec("png")  // Usando PNG para os frames
                .done();

        // Executor do FFmpeg
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Executa o trabalho de extração dos frames
        executor.createJob(builder).run();

        // Cria o arquivo .zip contendo as imagens
        File zipFile = new File(OUTPUT_DIR, "frames.zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            File[] files = new File(OUTPUT_DIR).listFiles((dir, name) -> name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }

        // Limpeza dos arquivos temporários (frames)
        FileUtils.cleanDirectory(new File(OUTPUT_DIR));

        return zipFile;
    }

}
