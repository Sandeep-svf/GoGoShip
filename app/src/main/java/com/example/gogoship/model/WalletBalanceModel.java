package com.mobapps.gogoship.model;

import com.mobapps.gogoship.response.WalletBalanceResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("My_Wallet")
    @Expose
    private WalletBalanceResponse walletBalanceResponse;

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

    public WalletBalanceResponse getWalletBalanceResponse() {
        return walletBalanceResponse;
    }

    public void setWalletBalanceResponse(WalletBalanceResponse walletBalanceResponse) {
        this.walletBalanceResponse = walletBalanceResponse;
    }
}
