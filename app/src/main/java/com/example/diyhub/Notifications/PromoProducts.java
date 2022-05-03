package com.example.diyhub.Notifications;

public class PromoProducts {
    String itemImage,productName;
    long purchases;

    public PromoProducts(String itemImage, String productName, long purchases) {
        this.itemImage = itemImage;
        this.productName = productName;
        this.purchases = purchases;
    }

    public PromoProducts(){}

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getPurchases() {
        return purchases;
    }

    public void setPurchases(long purchases) {
        this.purchases = purchases;
    }
}
