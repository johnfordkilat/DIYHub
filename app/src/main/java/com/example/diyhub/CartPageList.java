package com.example.diyhub;

public class CartPageList {
    String ProductID,ProductImage,ProductName;
    double ProductPrice;
    int ProductQuantity;
    double TotalPrice;
    String SellerID;
    String Variations;
    String ShopName;
    double ProductShippingFee;
    double ProductAdditionalFee;
    String ProductType;

    public CartPageList() {
    }

    public CartPageList(String productID, String productImage, String productName, double productPrice, int productQuantity, double totalPrice,
                        String sellerID, String variations, String shopName, double productShippingFee, double productAdditionalFee, String productType) {
        ProductID = productID;
        ProductImage = productImage;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductQuantity = productQuantity;
        TotalPrice = totalPrice;
        SellerID = sellerID;
        Variations = variations;
        ShopName = shopName;
        ProductShippingFee = productShippingFee;
        ProductAdditionalFee = productAdditionalFee;
        ProductType = productType;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
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

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getVariations() {
        return Variations;
    }

    public void setVariations(String variations) {
        Variations = variations;
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
