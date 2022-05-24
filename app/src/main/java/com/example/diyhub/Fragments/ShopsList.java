package com.example.diyhub.Fragments;

public class ShopsList {
    String SellerID;
    String ShopAddress;
    String ShopName;
    double ShopRating;
    String ShopImage;

    public ShopsList() {
    }

    public ShopsList(String sellerID, String shopAddress, String shopName, double shopRating, String shopImage) {
        SellerID = sellerID;
        ShopAddress = shopAddress;
        ShopName = shopName;
        ShopRating = shopRating;
        ShopImage = shopImage;
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
}
