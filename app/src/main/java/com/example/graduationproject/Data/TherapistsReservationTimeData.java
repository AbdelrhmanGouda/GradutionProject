package com.example.graduationproject.Data;

public class TherapistsReservationTimeData {
    private String startTime,endTime;


    public TherapistsReservationTimeData() {
    }

    public TherapistsReservationTimeData(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
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
}
