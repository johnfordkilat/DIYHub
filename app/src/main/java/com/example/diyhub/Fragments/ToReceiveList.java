package com.example.diyhub.Fragments;

import android.os.Parcel;
import android.os.Parcelable;

public class ToReceiveList implements Parcelable {

    String OrderProductName, OrderQuantity;
    String OrderProductImage,OrderID,OrderType,PaymentOption;
    String ItemCode, BuyerName, PaymentStatus, OrderDate;
    String BuyerImage;
    String RiderName,PlateNumber;

    public ToReceiveList(){

    }

    public ToReceiveList(String productName, String productQuantity, String prodImage, String prodID, String orderType, String paymentOption, String itemCode,
                      String buyerName, String paymentStatus, String orderDate, String buyerImage, String riderName, String plateNumber ) {
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
        RiderName = riderName;
        PlateNumber = plateNumber;

    }

    public String getRiderName() {
        return RiderName;
    }

    public void setRiderName(String riderName) {
        RiderName = riderName;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
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
        dest.writeString(PlateNumber);
        dest.writeString(RiderName);

    }

    public ToReceiveList(Parcel in){
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
        RiderName = in.readString();
        PlateNumber = in.readString();
    }

    public static final Parcelable.Creator<ToReceiveList> CREATOR = new Parcelable.Creator<ToReceiveList>() {
        public ToReceiveList createFromParcel(Parcel in) {
            return new ToReceiveList(in);
        }

        public ToReceiveList[] newArray(int size) {
            return new ToReceiveList[size];
        }
    };
}
