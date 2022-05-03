package com.example.diyhub;

public class RestockProductsList {

    String ProductName, ProductQuantity, ProductStocks;
    String ProductImage,ProductID,ProductImageStatus,ProductStatus;

    public RestockProductsList(){

    }

    public RestockProductsList(String productName, String productQuantity, String productStocks, String prodImage, String prodID, String prodImgStatus, String prodStatus) {
        ProductName = productName;
        ProductQuantity = productQuantity;
        ProductStocks = productStocks;
        ProductImage = prodImage;
        ProductID = prodID;
        ProductImageStatus = prodImgStatus;
        ProductStatus = prodStatus;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public void setProductStocks(String productStocks) {
        ProductStocks = productStocks;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public void setProductImageStatus(String productImageStatus) {
        ProductImageStatus = productImageStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public String getProductStocks() {
        return ProductStocks;
    }
    public String getProductImage() {
        return ProductImage;
    }
    public String getProductID(){return ProductID;}
    public String getProductImageStatus(){return ProductImageStatus;}
    public String getProductStatus(){return ProductStatus;}

    public void setProductID(String prodID)
    {
        this.ProductID = prodID;
    }
}
