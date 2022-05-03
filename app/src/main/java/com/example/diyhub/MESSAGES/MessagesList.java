package com.example.diyhub.MESSAGES;

public class MessagesList {


    String profileName;
    String profileImage;

    String profileEmail;

    public  MessagesList(){}

    public MessagesList(String profileName, String profileImage,String profileEmail) {
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.profileEmail = profileEmail;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileID) {
        this.profileEmail = profileID;
    }




}
