package com.example.diyhub;

public class FollowingListShopPageBuyer {
    String SellerID,ShopName;
    boolean isFollowed;

    public FollowingListShopPageBuyer() {
    }

    public FollowingListShopPageBuyer(String sellerID, String shopName, boolean isFollowed) {
        SellerID = sellerID;
        ShopName = shopName;
        this.isFollowed = isFollowed;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
