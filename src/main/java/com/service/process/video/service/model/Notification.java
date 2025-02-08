package com.service.process.video.service.model;

import com.service.process.video.service.enums.NotificationType;

import java.util.UUID;

public class Notification {

    String email;
    NotificationType notificationType;
    String bucketAddress;
    UUID videoId;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getBucketAddress() {
        return bucketAddress;
    }

    public void setBucketAddress(String bucketAddress) {
        this.bucketAddress = bucketAddress;
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
        this.videoId = videoId;
    }
}
