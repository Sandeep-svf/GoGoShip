package com.gogoship.gogoship.model;

import com.gogoship.gogoship.response.TransactionResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransacionListModel {

    @SerializedName("success")
    @Expose
    private String success;

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

    public List<TransactionResponse> getTransactionResponseList() {
        return transactionResponseList;
    }

    public void setTransactionResponseList(List<TransactionResponse> transactionResponseList) {
        this.transactionResponseList = transactionResponseList;
    }

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("WalletTxnDetails")
    @Expose
    private List<TransactionResponse> transactionResponseList = null;
}
