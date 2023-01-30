package com.mobapps.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionAmountList {

    @SerializedName("credit")
    @Expose
    private Double credit;
    @SerializedName("date")
    @Expose
    private String date;

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
