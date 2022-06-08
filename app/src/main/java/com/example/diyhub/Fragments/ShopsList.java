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
    int ShopViews;
    String BusinessType;

    public ShopsList() {
    }

    public ShopsList(String sellerID, String shopAddress, String shopName, double shopRating, String shopImage, String fullname, String shopMotto,
                     String latitude, String longitude, String distance, int ShopViews, String businessType) {
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
        ShopViews = ShopViews;
        BusinessType = businessType;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public int getShopViews() {
        return ShopViews;
    }

    public void setShopViews(int shopViews) {
        ShopViews = shopViews;
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
