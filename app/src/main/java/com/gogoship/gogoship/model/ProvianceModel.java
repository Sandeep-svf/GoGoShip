package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.ProvianceResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvianceModel {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Province")
    @Expose
    private List<ProvianceResponse> provianceResponseList = null;

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

    public List<ProvianceResponse> getProvianceResponseList() {
        return provianceResponseList;
    }

    public void setProvianceResponseList(List<ProvianceResponse> provianceResponseList) {
        this.provianceResponseList = provianceResponseList;
    }
}
