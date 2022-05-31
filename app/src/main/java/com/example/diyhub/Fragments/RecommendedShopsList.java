package com.example.diyhub.Fragments;

public class RecommendedShopsList {
    String SellerID, ShopImage, ShopName;
    Double ShopRating;
    int ShopViews;

    public RecommendedShopsList() {
    }

    public RecommendedShopsList(String sellerID, String shopImage, String shopName, Double shopRating, int shopViews) {
        SellerID = sellerID;
        ShopImage = shopImage;
        ShopName = shopName;
        ShopRating = shopRating;
        ShopViews = shopViews;
    }

    public int getShopViews() {
        return ShopViews;
    }

    public void setShopViews(int shopViews) {
        ShopViews = shopViews;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getShopImage() {
        return ShopImage;
    }

    public void setShopImage(String shopImage) {
        ShopImage = shopImage;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public Double getShopRating() {
        return ShopRating;
    }

    public void setShopRating(Double shopRating) {
        ShopRating = shopRating;
    }
}