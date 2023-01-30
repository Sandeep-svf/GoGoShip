package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.NotificationResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notification")
    @Expose
    private List<NotificationResponse> notificationResponseList = null;

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

    public List<NotificationResponse> getNotificationResponseList() {
        return notificationResponseList;
    }

    public void setNotificationResponseList(List<NotificationResponse> notificationResponseList) {
        this.notificationResponseList = notificationResponseList;
    }
}
