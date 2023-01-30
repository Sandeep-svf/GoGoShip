package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.ProfileData;
import com.mobapps.gogoship.response.ProfileDataResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private ProfileData profileData;

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

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public ProfileDataResponse getProfileDataResponse() {
        return profileDataResponse;
    }

    public void setProfileDataResponse(ProfileDataResponse profileDataResponse) {
        this.profileDataResponse = profileDataResponse;
    }

    @SerializedName("ProfileData")
    @Expose
    private ProfileDataResponse profileDataResponse;

}
