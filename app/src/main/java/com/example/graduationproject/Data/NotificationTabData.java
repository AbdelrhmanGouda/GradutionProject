package com.example.graduationproject.Data;

public class NotificationTabData {
    private String name,uri;

    public NotificationTabData(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public NotificationTabData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
