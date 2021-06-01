package com.example.graduationproject.Data;

public class AllAppointmentData {

    String dayDate,endTime,startTime,patientId,patientName, state,therapyId,therapyName,timeName;

    public AllAppointmentData() {
    }

    public AllAppointmentData(String dayDate, String endTime, String startTime
            , String patientId, String patientName, String status, String therapyId, String therapyName, String timeName) {
        this.dayDate = dayDate;
        this.endTime = endTime;
        this.startTime = startTime;
        this.patientId = patientId;
        this.patientName = patientName;
        this.state = status;
        this.therapyId = therapyId;
        this.therapyName = therapyName;
        this.timeName = timeName;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTherapyId() {
        return therapyId;
    }

    public void setTherapyId(String therapyId) {
        this.therapyId = therapyId;
    }

    public String getTherapyName() {
        return therapyName;
    }

    public void setTherapyName(String therapyName) {
        this.therapyName = therapyName;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
