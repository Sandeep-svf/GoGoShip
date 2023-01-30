package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.VarifyEmailResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VarifyEmailModel {

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

    public VarifyEmailResponse getVarifyEmailResponse() {
        return varifyEmailResponse;
    }

    public void setVarifyEmailResponse(VarifyEmailResponse varifyEmailResponse) {
        this.varifyEmailResponse = varifyEmailResponse;
    }

    @SerializedName("Data")
    @Expose
    private VarifyEmailResponse varifyEmailResponse;

}
