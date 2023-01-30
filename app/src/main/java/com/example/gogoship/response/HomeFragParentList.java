package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeFragParentList {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("data")
    @Expose
    public List<HomeFragChildList> homeFragChildListList = null;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<HomeFragChildList> getHomeFragChildListList() {
        return homeFragChildListList;
    }

    public void setHomeFragChildListList(List<HomeFragChildList> homeFragChildListList) {
        this.homeFragChildListList = homeFragChildListList;
    }
}
