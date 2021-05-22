package com.example.graduationproject.Data;

public class TherapistsByNameData {

    String name,phone, imageUrl,location,id;


    public TherapistsByNameData() {
    }

    public TherapistsByNameData(String name, String phone, String imageUrl, String location, String id) {
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.location = location;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
