package com.example.diyhub.Notifications;

public class UserNotif {

    public String id,username,imageUrl;
    public String status;

    public UserNotif(){}

    public UserNotif(String email, String username, String imageUrl, String status) {
        this.id = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.status = status;
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
