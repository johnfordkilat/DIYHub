package com.example.diyhub;

public class AllProductsList {

    String ProductName;
    String ProductImage,ProductID,ProductStatusImage,ProductStatus;
    String ProductType;
    double ProductPrice;
    double ProductSold;
    String ProductMaterial;
    String ProductDescription;
    int ProductQuantity;
    int ProductStocks;

    public AllProductsList(){

    }

    public AllProductsList(String productName, int productQuantity, int productStocks, String prodImage, String prodID, String prodImgStatus, String prodStatus,
                           String productType, double productPrice, double productSold, String productDescription, String productMaterial) {
        ProductName = productName;
        ProductQuantity = productQuantity;
        ProductStocks = productStocks;
        ProductImage = prodImage;
        ProductID = prodID;
        ProductStatusImage = prodImgStatus;
        ProductStatus = prodStatus;
        ProductType = productType;
        ProductPrice = productPrice;
        ProductSold = productSold;
        ProductDescription = productDescription;
        ProductMaterial = productMaterial;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public double getProductSold() {
        return ProductSold;
    }

    public void setProductSold(double productSold) {
        ProductSold = productSold;
    }

    public String getProductMaterial() {
        return ProductMaterial;
    }

    public void setProductMaterial(String productMaterial) {
        ProductMaterial = productMaterial;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public int getProductStocks() {
        return ProductStocks;
    }

    public void setProductStocks(int productStocks) {
        ProductStocks = productStocks;
    }
}
