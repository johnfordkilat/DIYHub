package com.example.diyhub.Notifications;

public class NotificationPromoList {

    private String NotifImage;
    private String NotifDescription;
    private String IsSeen;
    private String NotifID;
    private String NotifHeader;
    private String NotifDateAndTime;

    public NotificationPromoList(){}

    public NotificationPromoList(String notifImage, String notifDescription, String isSeen, String notifID, String notifHeader, String notifDateAndTime) {
        this.NotifImage = notifImage;
        this.NotifDescription = notifDescription;
        this.IsSeen = isSeen;
        this.NotifID = notifID;
        this.NotifHeader = notifHeader;
        this.NotifDateAndTime = notifDateAndTime;
    }

    public String getNotifHeader() {
        return NotifHeader;
    }

    public void setNotifHeader(String notifHeader) {
        NotifHeader = notifHeader;
    }

    public String getNotifDateAndTime() {
        return NotifDateAndTime;
    }

    public void setNotifDateAndTime(String notifDateAndTime) {
        NotifDateAndTime = notifDateAndTime;
    }

    public String getNotifID() {
        return NotifID;
    }

    public void setNotifID(String notifID) {
        NotifID = notifID;
    }

    public String getIsSeen() {
        return IsSeen;
    }

    public void setIsSeen(String isSeen) {
        IsSeen = isSeen;
    }

    public String getNotifImage() {
        return NotifImage;
    }



    public void setNotifImage(String notifImage) {
        NotifImage = notifImage;
    }

    public String getNotifDescription() {
        return NotifDescription;
    }

    public void setNotifDescription(String notifDescription) {
        NotifDescription = notifDescription;
    }
}
