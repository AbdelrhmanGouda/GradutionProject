package com.example.graduationproject.Data;

public class NotificationTabData {
    private String name;
    private String stateOfNotification;

    public NotificationTabData(String name, String stateOfNotification) {
        this.name = name;
        this.stateOfNotification = stateOfNotification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateOfNotification() {
        return stateOfNotification;
    }

    public void setStateOfNotification(String stateOfNotification) {
        this.stateOfNotification = stateOfNotification;
    }
}
