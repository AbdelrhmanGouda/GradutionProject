package com.example.graduationproject.Data;

public class TherapistsByNameData {

    String name,phone, imageUrl,location,id,cost;


    public TherapistsByNameData() {
    }

    public TherapistsByNameData(String name, String phone, String imageUrl, String location, String id, String cost) {
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.location = location;
        this.id = id;
        this.cost = cost;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
