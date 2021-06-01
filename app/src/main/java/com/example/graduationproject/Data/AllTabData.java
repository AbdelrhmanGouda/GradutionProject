package com.example.graduationproject.Data;

public class AllTabData {
    private String dayDate, startTime, endTime, therapyName, state;

    public AllTabData() {
    }

    public AllTabData(String dayDate, String startTime, String endTime, String therapyName, String state) {
        this.dayDate = dayDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.therapyName = therapyName;
        this.state = state;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTherapyName() {
        return therapyName;
    }

    public void setTherapyName(String therapyName) {
        this.therapyName = therapyName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}