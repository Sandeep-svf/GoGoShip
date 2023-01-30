package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.ToWhereResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToWhereModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ToWhere")
    @Expose
    private List<ToWhereResponse> toWhereResponseList = null;

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

    public List<ToWhereResponse> getToWhereResponseList() {
        return toWhereResponseList;
    }

    public void setToWhereResponseList(List<ToWhereResponse> toWhereResponseList) {
        this.toWhereResponseList = toWhereResponseList;
    }
}
