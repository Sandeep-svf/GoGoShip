package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.ArchiveResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArchiveModel {

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

    public List<ArchiveResponse> getArchiveResponseList() {
        return archiveResponseList;
    }

    public void setArchiveResponseList(List<ArchiveResponse> archiveResponseList) {
        this.archiveResponseList = archiveResponseList;
    }

    @SerializedName("Archive_Order")
    @Expose
    private List<ArchiveResponse> archiveResponseList = null;

}
