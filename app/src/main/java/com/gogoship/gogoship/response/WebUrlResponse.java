package com.gogoship.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebUrlResponse {
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPrice")
    @Expose
    private String productPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @SerializedName("productImage")
    @Expose
    private String productImage;
}
