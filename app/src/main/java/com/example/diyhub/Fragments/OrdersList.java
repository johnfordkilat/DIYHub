package com.example.diyhub.Fragments;

import android.os.Parcel;
import android.os.Parcelable;

public class OrdersList implements Parcelable {

    String OrderProductName;
    String OrderProductImage,OrderID,OrderType,PaymentOption;
    String ItemCode, BuyerName, PaymentStatus, OrderDate;
    String BuyerImage;
    String BookingAddress;
    String OrderStatus;
    String ShopName;
    String DeliveryType;
    String RiderName,PlateNumber;
    String BookingOption;
    String BuyerID;
    String PaymentReference;
    String OrderDeclineReason;
    double OrderProductPrice;
    double OrderShippingFee;
    double OrderAdditionalFee;
    double OrderTotalPayment;
    int OrderQuantity;

    public OrdersList(){

    }

    public OrdersList(String productName, int orderQuantity, String prodImage, String prodID, String orderType, String paymentOption, String itemCode,
                       String buyerName, String paymentStatus, String orderDate, String buyerImage, String bookingAddress, String orderStatus, String shopName,
                      String deliveryType, String riderName, String plateNumber, String bookingOption, double orderProductPrice, double orderShippingFee, double orderAdditionalFee,
                        double orderTotalPayment, String buyerID, String paymentReference, String orderDeclineReason) {
        OrderProductName = productName;
        OrderQuantity = orderQuantity;
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
        OrderStatus = orderStatus;
        ShopName = shopName;
        DeliveryType = deliveryType;
        RiderName = riderName;
        PlateNumber = plateNumber;
        BookingOption = bookingOption;
        OrderProductPrice = orderProductPrice;
        OrderShippingFee = orderShippingFee;
        OrderAdditionalFee = orderAdditionalFee;
        OrderTotalPayment = orderTotalPayment;
        BuyerID = buyerID;
        PaymentReference = paymentReference;
        OrderDeclineReason = orderDeclineReason;


    }

    public void setOrderQuantity(int orderQuantity) {
        OrderQuantity = orderQuantity;
    }

    public String getOrderDeclineReason() {
        return OrderDeclineReason;
    }

    public void setOrderDeclineReason(String orderDeclineReason) {
        OrderDeclineReason = orderDeclineReason;
    }

    public String getPaymentReference() {
        return PaymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        PaymentReference = paymentReference;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        BuyerID = buyerID;
    }

    public double getOrderProductPrice() {
        return OrderProductPrice;
    }

    public void setOrderProductPrice(double orderProductPrice) {
        OrderProductPrice = orderProductPrice;
    }

    public double getOrderShippingFee() {
        return OrderShippingFee;
    }

    public void setOrderShippingFee(double orderShippingFee) {
        OrderShippingFee = orderShippingFee;
    }

    public double getOrderAdditionalFee() {
        return OrderAdditionalFee;
    }

    public void setOrderAdditionalFee(double orderAdditionalFee) {
        OrderAdditionalFee = orderAdditionalFee;
    }

    public double getOrderTotalPayment() {
        return OrderTotalPayment;
    }

    public void setOrderTotalPayment(double orderTotalPayment) {
        OrderTotalPayment = orderTotalPayment;
    }

    public String getBookingOption() {
        return BookingOption;
    }

    public void setBookingOption(String bookingOption) {
        BookingOption = bookingOption;
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

    public String getDeliveryType() {
        return DeliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        DeliveryType = deliveryType;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
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

    public int getOrderQuantity() {
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
        dest.writeInt(OrderQuantity);
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
        dest.writeString(OrderStatus);
        dest.writeString(ShopName);
        dest.writeString(DeliveryType);
        dest.writeString(RiderName);
        dest.writeString(PlateNumber);
        dest.writeString(BookingOption);
        dest.writeDouble(OrderProductPrice);
        dest.writeDouble(OrderAdditionalFee);
        dest.writeDouble(OrderShippingFee);
        dest.writeDouble(OrderTotalPayment);
        dest.writeString(BuyerID);
        dest.writeString(PaymentReference);
        dest.writeString(OrderDeclineReason);

    }

    public OrdersList(Parcel in){
        OrderProductName = in.readString();
        OrderQuantity = in.readInt();
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
        OrderStatus = in.readString();
        ShopName = in.readString();
        DeliveryType = in.readString();
        RiderName = in.readString();
        PlateNumber = in.readString();
        BookingOption = in.readString();
        OrderProductPrice = in.readDouble();
        OrderAdditionalFee = in.readDouble();
        OrderShippingFee = in.readDouble();
        OrderTotalPayment = in.readDouble();
        BuyerID = in.readString();
        PaymentReference = in.readString();
        OrderDeclineReason = in.readString();
    }

    public static final Parcelable.Creator<OrdersList> CREATOR = new Parcelable.Creator<OrdersList>() {
        public OrdersList createFromParcel(Parcel in) {
            return new OrdersList(in);
        }

        public OrdersList[] newArray(int size) {
            return new OrdersList[size];
        }
    };
}
