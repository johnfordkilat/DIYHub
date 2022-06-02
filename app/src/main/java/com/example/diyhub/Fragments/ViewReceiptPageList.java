package com.example.diyhub.Fragments;

public class ViewReceiptPageList {
    String productName;
    double totalPrice;
    double price;
    int quantity;

    public ViewReceiptPageList() {
    }

    public ViewReceiptPageList(String productName, double totalPrice, double price, int quantity) {
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
