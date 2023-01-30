package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceResponse {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ttl_balance")
    @Expose
    private Double ttlBalance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getTtlBalance() {
        return ttlBalance;
    }

    public void setTtlBalance(Double ttlBalance) {
        this.ttlBalance = ttlBalance;
    }

    public Double getTtlPurchaseAmount() {
        return ttlPurchaseAmount;
    }

    public void setTtlPurchaseAmount(Double ttlPurchaseAmount) {
        this.ttlPurchaseAmount = ttlPurchaseAmount;
    }

    @SerializedName("ttl_purchase_amount")
    @Expose
    private Double ttlPurchaseAmount;

}
