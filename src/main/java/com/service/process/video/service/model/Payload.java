package com.service.process.video.service.model;

import java.util.UUID;

public class Payload {
    UUID id;
    String email;
    String url;

    String s3Key;

    public Payload() {
    }

    public Payload(UUID id, String email, String url, String s3Key) {
        this.id = id;
        this.email = email;
        this.url = url;
        this.s3Key = s3Key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

}