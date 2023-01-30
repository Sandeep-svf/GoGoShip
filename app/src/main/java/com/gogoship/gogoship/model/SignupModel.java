package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.SignupDataResponse;
import com.gogoship.gogoship.response.SignupTempResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private SignupDataResponse signupDataResponse;
    @SerializedName("Temp")
    @Expose
    private SignupTempResponse signupTempResponse;

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

    public SignupDataResponse getSignupDataResponse() {
        return signupDataResponse;
    }

    public void setSignupDataResponse(SignupDataResponse signupDataResponse) {
        this.signupDataResponse = signupDataResponse;
    }

    public SignupTempResponse getSignupTempResponse() {
        return signupTempResponse;
    }

    public void setSignupTempResponse(SignupTempResponse signupTempResponse) {
        this.signupTempResponse = signupTempResponse;
    }
}
