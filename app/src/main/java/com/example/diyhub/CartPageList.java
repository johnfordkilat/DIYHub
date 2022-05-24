package com.example.diyhub;

public class CartPageList {
    String ProductID,ProductImage,ProductName;
    double ProductPrice;
    int ProductQuantity;
    double TotalPrice;

    public CartPageList() {
    }

    public CartPageList(String productID, String productImage, String productName, double productPrice, int productQuantity, double totalPrice) {
        ProductID = productID;
        ProductImage = productImage;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductQuantity = productQuantity;
        TotalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }
}
