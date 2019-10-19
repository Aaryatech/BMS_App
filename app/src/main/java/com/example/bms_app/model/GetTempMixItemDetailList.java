package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTempMixItemDetailList {
    @SerializedName("tempMixItemDetail")
    @Expose
    private List<TempMixItemDetail> tempMixItemDetail = null;

    public List<TempMixItemDetail> getTempMixItemDetail() {
        return tempMixItemDetail;
    }

    public void setTempMixItemDetail(List<TempMixItemDetail> tempMixItemDetail) {
        this.tempMixItemDetail = tempMixItemDetail;
    }

    @Override
    public String toString() {
        return "GetTempMixItemDetailList{" +
                "tempMixItemDetail=" + tempMixItemDetail +
                '}';
    }
}
