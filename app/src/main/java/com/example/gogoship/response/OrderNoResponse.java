package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderNoResponse {

    public Object getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Object orderNumber) {
        this.orderNumber = orderNumber;
    }

    @SerializedName("order_number")
    @Expose
    private Object orderNumber;
}
