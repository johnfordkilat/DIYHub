package com.example.diyhub.PastTransaction;

public class CompletedOrderList {

    String date,productName,shopName,productImage;

    public CompletedOrderList(){

    }

    public CompletedOrderList(String date, String productName, String shopName, String productImage) {
        this.date = date;
        this.productName = productName;
        this.shopName = shopName;
        this.productImage = productImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
