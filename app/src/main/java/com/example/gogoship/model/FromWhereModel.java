package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.FromWhereResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FromWhereModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("FromWhere")
    @Expose
    private List<FromWhereResponse> fromWhereResponseList = null;

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

    public List<FromWhereResponse> getFromWhereResponseList() {
        return fromWhereResponseList;
    }

    public void setFromWhereResponseList(List<FromWhereResponse> fromWhereResponseList) {
        this.fromWhereResponseList = fromWhereResponseList;
    }
}
