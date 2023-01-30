package com.gogoship.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {

    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("street")
    @Expose
    private Object street;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("province")
    @Expose
    private Object province;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }
}
