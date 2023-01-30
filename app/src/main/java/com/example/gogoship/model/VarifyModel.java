package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.ResuerUserIdResponse;
import com.mobapps.gogoship.response.VarifyResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VarifyModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private ResuerUserIdResponse resuerUserIdResponse;

    public ResuerUserIdResponse getResuerUserIdResponse() {
        return resuerUserIdResponse;
    }

    public void setResuerUserIdResponse(ResuerUserIdResponse resuerUserIdResponse) {
        this.resuerUserIdResponse = resuerUserIdResponse;
    }

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
}
