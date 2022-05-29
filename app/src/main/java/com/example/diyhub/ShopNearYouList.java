package com.example.diyhub;

public class ShopNearYouList {
    String name,image;
    String distance;

    public ShopNearYouList() {
    }

    public ShopNearYouList(String name, String image, String distance) {
        this.name = name;
        this.image = image;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
