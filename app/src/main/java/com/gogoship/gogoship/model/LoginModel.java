package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.LoginResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private LoginResponse loginResponse;

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

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }
}
