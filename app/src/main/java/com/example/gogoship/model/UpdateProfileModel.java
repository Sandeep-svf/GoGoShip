package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.UpdateProfileResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileModel {

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

    public UpdateProfileResponse getUpdateProfileResponse() {
        return updateProfileResponse;
    }

    public void setUpdateProfileResponse(UpdateProfileResponse updateProfileResponse) {
        this.updateProfileResponse = updateProfileResponse;
    }

    @SerializedName("Data")
    @Expose
    private UpdateProfileResponse updateProfileResponse;

}
