package com.gogoship.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupTempResponse {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("city")
    @Expose
    private String city;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
