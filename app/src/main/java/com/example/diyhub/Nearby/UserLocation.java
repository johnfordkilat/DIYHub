package com.example.diyhub.Nearby;

public class UserLocation {
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public UserLocation() {

    }

    public UserLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
