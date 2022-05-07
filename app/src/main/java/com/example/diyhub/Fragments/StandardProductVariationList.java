package com.example.diyhub.Fragments;

public class StandardProductVariationList {

    private String variationName;
    private String variationImage;

    public StandardProductVariationList(){}

    public StandardProductVariationList(String variationName, String variationImage) {
        this.variationName = variationName;
        this.variationImage = variationImage;
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public String getVariationImage() {
        return variationImage;
    }

    public void setVariationImage(String variationImage) {
        this.variationImage = variationImage;
    }
}
