package com.example.diyhub.Wallet;

public class WalletList {
    double Balance;

    public WalletList() {
    }

    public WalletList(double balance) {
        Balance = balance;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }
}
