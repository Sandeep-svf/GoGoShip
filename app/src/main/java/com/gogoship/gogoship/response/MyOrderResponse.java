package com.gogoship.gogoship.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrderResponse {


    @SerializedName("myOrder")
    @Expose
    public List<MyOrderList> myOrderLists = null;



    public List<MyOrderList> getMyOrderLists() {
        return myOrderLists;
    }

    public void setMyOrderLists(List<MyOrderList> myOrderLists) {
        this.myOrderLists = myOrderLists;
    }

    public List<MyOrderDetailList> getMyOrderDetailLists() {
        return myOrderDetailLists;
    }

    public void setMyOrderDetailLists(List<MyOrderDetailList> myOrderDetailLists) {
        this.myOrderDetailLists = myOrderDetailLists;
    }

    @SerializedName("myOrderDetail")
    @Expose
    public List<MyOrderDetailList> myOrderDetailLists = null;

}
