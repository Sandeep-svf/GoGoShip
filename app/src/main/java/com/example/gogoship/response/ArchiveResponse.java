package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArchiveResponse {


    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countryflag")
    @Expose
    private String countryflag;
    @SerializedName("orderedFrom")
    @Expose
    private String orderedFrom;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("product_link")
    @Expose
    private String productLink;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("color")
    @Expose
    private Object color;
    @SerializedName("size")
    @Expose
    private Object size;
    @SerializedName("total")
    @Expose
    private String total;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryflag() {
        return countryflag;
    }

    public void setCountryflag(String countryflag) {
        this.countryflag = countryflag;
    }

    public String getOrderedFrom() {
        return orderedFrom;
    }

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Object getColor() {
        return color;
    }

    public void setColor(Object color) {
        this.color = color;
    }

    public Object getSize() {
        return size;
    }

    public void setSize(Object size) {
        this.size = size;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("fees")
    @Expose
    private String fees;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("insurance")
    @Expose
    private String insurance;
    @SerializedName("status")
    @Expose
    private String status;


}
