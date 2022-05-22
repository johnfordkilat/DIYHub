package com.example.diyhub.Buyer;

public class AddBookingModel {

    String address;
    String landmark;

    public AddBookingModel() {
    }

    public AddBookingModel(String address, String landmark) {
        this.address = address;
        this.landmark = landmark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
