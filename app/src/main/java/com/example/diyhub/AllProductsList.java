package com.example.diyhub;

import com.example.diyhub.Fragments.ShopsList;

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
    double ProductRating;
    String ProductBookFrom;
    String SellerID;
    String ShopName;
    double ProductShippingFee;
    double ProductAdditionalFee;
    double ProductTotalPayment;
    String ProductCategory;

    public AllProductsList(){

    }

    public AllProductsList(String productName, int productQuantity, int productStocks, String prodImage, String prodID, String prodImgStatus, String prodStatus,
                           String productType, double productPrice, double productSold, String productDescription, String productMaterial, double productRating,
                           String productBookFrom, String sellerID, String shopName, double productShippingFee, double productAdditionalFee, double productTotalPayment,
                           String productCategory) {
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
        ProductRating = productRating;
        ProductBookFrom = productBookFrom;
        SellerID = sellerID;
        ShopName = shopName;
        ProductShippingFee = productShippingFee;
        ProductAdditionalFee = productAdditionalFee;
        ProductTotalPayment = productTotalPayment;
        ProductCategory = productCategory;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public double getProductShippingFee() {
        return ProductShippingFee;
    }

    public void setProductShippingFee(double productShippingFee) {
        ProductShippingFee = productShippingFee;
    }

    public double getProductAdditionalFee() {
        return ProductAdditionalFee;
    }

    public void setProductAdditionalFee(double productAdditionalFee) {
        ProductAdditionalFee = productAdditionalFee;
    }

    public double getProductTotalPayment() {
        return ProductTotalPayment;
    }

    public void setProductTotalPayment(double productTotalPayment) {
        ProductTotalPayment = productTotalPayment;
    }

    public String getSellerID() {
        return SellerID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getProductBookFrom() {
        return ProductBookFrom;
    }

    public void setProductBookFrom(String productBookFrom) {
        ProductBookFrom = productBookFrom;
    }

    public double getProductRating() {
        return ProductRating;
    }

    public void setProductRating(double productRating) {
        ProductRating = productRating;
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
