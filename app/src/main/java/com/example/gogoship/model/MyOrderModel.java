package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.MyOrderResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderModel {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("My_Order")
    @Expose
    public MyOrderResponse myOrderResponse;

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

    public MyOrderResponse getMyOrderResponse() {
        return myOrderResponse;
    }

    public void setMyOrderResponse(MyOrderResponse myOrderResponse) {
        this.myOrderResponse = myOrderResponse;
    }
}
