package com.example.diyhub.Fragments;

public class OrderCustomizationsSpecs {
    String Color;
    String Image;
    String CustomerRequest;

    public OrderCustomizationsSpecs() {
    }

    public OrderCustomizationsSpecs(String color, String image, String customerRequest) {
        Color = color;
        Image = image;
        CustomerRequest = customerRequest;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCustomerRequest() {
        return CustomerRequest;
    }

    public void setCustomerRequest(String customerRequest) {
        CustomerRequest = customerRequest;
    }
}
