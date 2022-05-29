package com.example.diyhub.Fragments;

import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomSpecsSellerList {
    String specsLabel;
    String bigImage;

    public AddCustomSpecsSellerList() {
    }

    public AddCustomSpecsSellerList(String specsLabel, String bigImage) {
        this.specsLabel = specsLabel;
        this.bigImage = bigImage;
    }

    public String getSpecsLabel() {
        return specsLabel;
    }

    public void setSpecsLabel(String specsLabel) {
        this.specsLabel = specsLabel;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }
}
