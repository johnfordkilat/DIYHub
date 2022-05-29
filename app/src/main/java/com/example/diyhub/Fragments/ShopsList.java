package com.example.diyhub.Fragments;

public class ShopsList {
    String SellerID;
    String ShopAddress;
    String ShopName;
    double ShopRating;
    String ShopImage;
    String Fullname;
    String ShopMotto;
    String latitude,longitude;
    String distance;

    public ShopsList() {
    }

    public ShopsList(String sellerID, String shopAddress, String shopName, double shopRating, String shopImage, String fullname, String shopMotto,
                     String latitude, String longitude, String distance) {
        SellerID = sellerID;
        ShopAddress = shopAddress;
        ShopName = shopName;
        ShopRating = shopRating;
        ShopImage = shopImage;
        Fullname = fullname;
        ShopMotto = shopMotto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public double getShopRating() {
        return ShopRating;
    }

    public void setShopRating(double shopRating) {
        ShopRating = shopRating;
    }

    public String getShopImage() {
        return ShopImage;
    }

    public void setShopImage(String shopImage) {
        ShopImage = shopImage;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getShopMotto() {
        return ShopMotto;
    }

    public void setShopMotto(String shopMotto) {
        ShopMotto = shopMotto;
    }
}
