package com.example.graduationproject.Data;

import android.media.Image;

public class TestData {
    String rightName;
    int rightImage;

    public TestData() {
    }

    public TestData(String rightName, int rightImage) {
        this.rightName = rightName;
        this.rightImage = rightImage;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public int getRightImage() {
        return rightImage;
    }

    public void setRightImage(int rightImage) {
        this.rightImage = rightImage;
    }
}
