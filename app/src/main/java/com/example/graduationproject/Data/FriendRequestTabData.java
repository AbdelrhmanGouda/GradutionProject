package com.example.graduationproject.Data;

public class FriendRequestTabData {
    private String name ,uri ,id,state;

    public FriendRequestTabData(String name, String uri, String id, String state) {
        this.name = name;
        this.uri = uri;
        this.id = id;
        this.state = state;
    }

    public FriendRequestTabData() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
