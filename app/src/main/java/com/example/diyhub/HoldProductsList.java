package com.example.diyhub;

public class HoldProductsList {

    String ProductName, ProductQuantity, ProductStocks;
    String ProductImage,ProductID,ProductStatusImage,ProductStatus;

    public HoldProductsList(){

    }

    public HoldProductsList(String productName, String productQuantity, String productStocks, String prodImage, String prodID, String prodImgStatus, String prodStatus) {
        ProductName = productName;
        ProductQuantity = productQuantity;
        ProductStocks = productStocks;
        ProductImage = prodImage;
        ProductID = prodID;
        ProductStatusImage = prodImgStatus;
        ProductStatus = prodStatus;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductStocks() {
        return ProductStocks;
    }

    public void setProductStocks(String productStocks) {
        ProductStocks = productStocks;
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

    public String getProductStatusImage() {
        return ProductStatusImage;
    }

    public void setProductStatusImage(String productStatusImage) {
        ProductStatusImage = productStatusImage;
    }

    public String getProductStatus() {
        return ProductStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }
}
