package com.service.process.video.service.model;

import java.util.UUID;

public class Payload {
    UUID id;
    String email;
    String url;

    String key;

    public Payload() {
    }

    public Payload(UUID id, String email, String url, String s3Key) {
        this.id = id;
        this.email = email;
        this.url = url;
        this.key = s3Key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}