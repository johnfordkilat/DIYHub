package com.example.diyhub;

public class AllProductsList {

    String ProductName, ProductQuantity, ProductStocks;
    String ProductImage,ProductID,ProductImageStatus,ProductStatus;

    public AllProductsList(){

    }

    public AllProductsList(String productName, String productQuantity, String productStocks, String prodImage, String prodID, String prodImgStatus, String prodStatus) {
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
    public void setProductStatus(String status)
    {
        this.ProductStatus = status;
    }
    public void setProductID(String prodID)
    {
        this.ProductID = prodID;
    }
}
