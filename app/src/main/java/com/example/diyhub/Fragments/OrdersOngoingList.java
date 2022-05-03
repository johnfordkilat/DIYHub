package com.example.diyhub.Fragments;

import android.os.Parcel;
import android.os.Parcelable;

public class OrdersOngoingList implements Parcelable {

    String OrderProductName, OrderQuantity;
    String OrderProductImage,OrderID,OrderType,PaymentOption;
    String ItemCode, BuyerName, PaymentStatus, OrderDate;
    String BuyerImage;
    String BookingAddress;

    public OrdersOngoingList(){

    }

    public OrdersOngoingList(String productName, String productQuantity, String prodImage, String prodID, String orderType, String paymentOption, String itemCode,
                      String buyerName, String paymentStatus, String orderDate, String buyerImage, String bookingAddress ) {
        OrderProductName = productName;
        OrderQuantity = productQuantity;
        OrderProductImage = prodImage;
        OrderID = prodID;
        OrderType = orderType;
        PaymentOption = paymentOption;
        ItemCode = itemCode;
        BuyerName = buyerName;
        PaymentStatus = paymentStatus;
        OrderDate = orderDate;
        BuyerImage = buyerImage;
        BookingAddress = bookingAddress;

    }

    public String getBookingAddress() {
        return BookingAddress;
    }

    public void setBookingAddress(String bookingAddress) {
        BookingAddress = bookingAddress;
    }

    public String getOrderProductName() {
        return OrderProductName;
    }

    public String getOrderQuantity() {
        return OrderQuantity;
    }

    public String getOrderType() {
        return OrderType;
    }

    public String getPaymentOption() {
        return PaymentOption;
    }

    public String getOrderProductImage() {
        return OrderProductImage;
    }
    public String getProductID(){return OrderID;}

    public void setProductID(String prodID)
    {
        this.OrderID = prodID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getBuyerImage() {
        return BuyerImage;
    }

    public void setBuyerImage(String buyerImage) {
        BuyerImage = buyerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderProductName);
        dest.writeString(OrderQuantity);
        dest.writeString(OrderProductImage);
        dest.writeString(OrderID);
        dest.writeString(OrderType);
        dest.writeString(PaymentOption);
        dest.writeString(ItemCode);
        dest.writeString(BuyerName);
        dest.writeString(PaymentStatus);
        dest.writeString(OrderDate);
        dest.writeString(BuyerImage);
        dest.writeString(BookingAddress);

    }

    public OrdersOngoingList(Parcel in){
        OrderProductName = in.readString();
        OrderQuantity = in.readString();
        OrderProductImage = in.readString();
        OrderID = in.readString();
        OrderType = in.readString();
        PaymentOption = in.readString();
        ItemCode = in.readString();
        BuyerName = in.readString();
        PaymentStatus = in.readString();
        OrderDate = in.readString();
        BuyerImage = in.readString();
        BookingAddress = in.readString();
    }

    public static final Parcelable.Creator<OrdersOngoingList> CREATOR = new Parcelable.Creator<OrdersOngoingList>() {
        public OrdersOngoingList createFromParcel(Parcel in) {
            return new OrdersOngoingList(in);
        }

        public OrdersOngoingList[] newArray(int size) {
            return new OrdersOngoingList[size];
        }
    };
}
