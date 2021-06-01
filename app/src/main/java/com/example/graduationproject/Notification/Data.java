package com.example.graduationproject.Notification;

public class Data {
    private String Title, message;

    public Data(String title, String message) {
        this.Title = title;
        this.message = message;
    }

    public Data() {
    }

    public String getTitle() {  
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getMessages() {
        return message;
    }

    public void setMessages(String messages) {
        this.message = messages;
    }
}
