package com.example.graduationproject.Data;

public class AllTabData {
    private String date,time,state,docName;

    public AllTabData(String date, String time, String state, String docName) {
        this.date = date;
        this.time = time;
        this.state = state;
        this.docName = docName;
    }

    public String getDate() {
        return date;
    }

    public void setData(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
