package com.example.diyhub.Fragments;

public class SubscriptionList {
    private String SubscriptionStatus;
    private String PaymentID;

    public SubscriptionList(){}

    public SubscriptionList(String subscriptionStatus, String paymentID) {
        SubscriptionStatus = subscriptionStatus;
        PaymentID = paymentID;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    public String getSubscriptionStatus() {
        return SubscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        SubscriptionStatus = subscriptionStatus;
    }
}
