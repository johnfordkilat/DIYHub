package com.example.diyhub;

public class USERPROFILE {

    public String UserID,UserFirstname,UserLastname,UserBirthdate,UserGender,UserPhone,UserAddress,UserType,UserStatus,UserEmail,UserUsername,UserPassword;

    public USERPROFILE()
    {

    }
    public USERPROFILE(String id,String firstname1, String lastname1, String birthdate1, String gender1, String phone1, String address1, String type1, String status1, String email1, String username1, String password1)
    {
        this.UserID = id;
        this.UserFirstname = firstname1;
        this.UserLastname = lastname1;
        this.UserBirthdate = birthdate1;
        this.UserGender = gender1;
        this.UserPhone = phone1;
        this.UserAddress = address1;
        this.UserType = type1;
        this.UserStatus = status1;
        this.UserEmail = email1;
        this.UserUsername = username1;
        this.UserPassword = password1;
    }
}
