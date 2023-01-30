package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.OrderNoResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderNoModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<OrderNoResponse> orderNoResponseList = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderNoResponse> getOrderNoResponseList() {
        return orderNoResponseList;
    }

    public void setOrderNoResponseList(List<OrderNoResponse> orderNoResponseList) {
        this.orderNoResponseList = orderNoResponseList;
    }
}
