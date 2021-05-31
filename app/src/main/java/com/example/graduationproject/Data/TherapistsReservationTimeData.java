package com.example.graduationproject.Data;

public class TherapistsReservationTimeData {
    private String startTime,endTime,timeName, dayDate;


    public TherapistsReservationTimeData() {
    }

    public TherapistsReservationTimeData(String startTime, String endTime, String timeName, String dayName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeName = timeName;
        this.dayDate = dayName;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
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
