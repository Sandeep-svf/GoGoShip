package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderList {

    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("shipped_date")
    @Expose
    private String shippedDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("attachment")
    @Expose
    private String attachment;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
