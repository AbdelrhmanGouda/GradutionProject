package com.example.graduationproject.Data;

public class TestDegree {

    private String illness,totalDegree;

    public TestDegree() {
    }

    public TestDegree(String illness, String totalDegree) {

        this.illness = illness;
        this.totalDegree = totalDegree;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getTotalDegree() {
        return totalDegree;
    }

    public void setTotalDegree(String totalDegree) {
        this.totalDegree = totalDegree;
    }
}
