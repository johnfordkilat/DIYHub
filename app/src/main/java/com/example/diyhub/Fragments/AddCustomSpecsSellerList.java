package com.example.diyhub.Fragments;

import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomSpecsSellerList {
    String bigImage;

    public AddCustomSpecsSellerList() {
    }

    public AddCustomSpecsSellerList(String bigImage) {
        this.bigImage = bigImage;
    }


    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }
}
