package com.example.diyhub.MESSAGES;

public class User {

    public String id,username,imageUrl;
    public String status;
    public String address;

    public User(){}

    public User(String email, String username, String imageUrl, String status, String address) {
        this.id = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.status = status;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
