package com.example.diyhub.Nearby;

public class DistanceList {
    private String name;
    private String distance;
    private String shopImage;

    public DistanceList(String name, String distance, String shopImage) {
        this.name = name;
        this.distance = distance;
        this.shopImage = shopImage;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public DistanceList() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
