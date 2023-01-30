package com.gogoship.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDataResponse {

    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("gender")
    @Expose
    private String gender;

}
