package com.example.diyhub;

public class FavoritesList {
    String ShopName, ProductName, SellerID, ProductImage, ProductID;
    boolean isFavorites;

    public FavoritesList() {
    }

    public FavoritesList(String shopName, String productName, String sellerID, String productImage, String productID, boolean isFavorites) {
        ShopName = shopName;
        ProductName = productName;
        SellerID = sellerID;
        ProductImage = productImage;
        ProductID = productID;
        this.isFavorites = isFavorites;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public boolean isFavorites() {
        return isFavorites;
    }

    public void setFavorites(boolean favorites) {
        isFavorites = favorites;
    }
}
