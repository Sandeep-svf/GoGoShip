package com.gogoship.gogoship.model;
import com.gogoship.gogoship.response.HomeFragParentList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeFragModel {

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

    public List<HomeFragParentList> getHomeFragParentListList() {
        return homeFragParentListList;
    }

    public void setHomeFragParentListList(List<HomeFragParentList> homeFragParentListList) {
        this.homeFragParentListList = homeFragParentListList;
    }

    @SerializedName("Home")
    @Expose
    public List<HomeFragParentList> homeFragParentListList = null;

}
