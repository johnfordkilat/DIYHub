package com.example.diyhub;

public class FollowingList {
    String ShopName, ShopImage, SellerID;
    boolean isFollowed;
    double ShopRating;

    public FollowingList() {

    }

    public FollowingList(String shopName, String shopImage, String sellerID, boolean isFollowed, double shopRating) {
        ShopName = shopName;
        ShopImage = shopImage;
        SellerID = sellerID;
        this.isFollowed = isFollowed;
        ShopRating = shopRating;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopImage() {
        return ShopImage;
    }

    public void setShopImage(String shopImage) {
        ShopImage = shopImage;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public double getShopRating() {
        return ShopRating;
    }

    public void setShopRating(double shopRating) {
        ShopRating = shopRating;
    }
}
