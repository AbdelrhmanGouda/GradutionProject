package com.example.graduationproject.Data;

public class TherapistsReservationTimeData {
    private String startTime,endTime,timeName,dayName;


    public TherapistsReservationTimeData() {
    }

    public TherapistsReservationTimeData(String startTime, String endTime, String timeName, String dayName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeName = timeName;
        this.dayName = dayName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
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
