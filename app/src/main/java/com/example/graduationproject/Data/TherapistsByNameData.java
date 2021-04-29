package com.example.graduationproject.Data;

public class TherapistsByNameData {

    String name,phone,image,location;


    public TherapistsByNameData() {
    }

    public TherapistsByNameData(String name, String phone, String image, String location) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
