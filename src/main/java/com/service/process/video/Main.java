package com.service.process.video;

import com.service.process.video.service.FrameZip;

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