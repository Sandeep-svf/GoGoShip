package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.SupportListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupportListModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

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

    public List<SupportListResponse> getSupportListResponseList() {
        return supportListResponseList;
    }

    public void setSupportListResponseList(List<SupportListResponse> supportListResponseList) {
        this.supportListResponseList = supportListResponseList;
    }

    @SerializedName("ticketList")
    @Expose
    private List<SupportListResponse> supportListResponseList = null;
}
