package com.example.diyhub.Fragments;

public class OrderAllItemSpecs {
    String Specs;
    String Image;

    public OrderAllItemSpecs() {
    }

    public OrderAllItemSpecs(String specs, String image) {
        Specs = specs;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSpecs() {
        return Specs;
    }

    public void setSpecs(String specs) {
        Specs = specs;
    }
}

