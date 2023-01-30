package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.WebUrlResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebsiteDetailModel {

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

    public WebUrlResponse getWebUrlResponse() {
        return webUrlResponse;
    }

    public void setWebUrlResponse(WebUrlResponse webUrlResponse) {
        this.webUrlResponse = webUrlResponse;
    }

    @SerializedName("webContents")
    @Expose
    private WebUrlResponse webUrlResponse;
}
