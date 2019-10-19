package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SfItemHeader {

    @SerializedName("sfItemDetail")
    @Expose
    private List<SfItemDetail> sfItemDetail = null;

    public List<SfItemDetail> getSfItemDetail() {
        return sfItemDetail;
    }

    public void setSfItemDetail(List<SfItemDetail> sfItemDetail) {
        this.sfItemDetail = sfItemDetail;
    }

    @Override
    public String toString() {
        return "sfItemHeader{" +
                "sfItemDetail=" + sfItemDetail +
                '}';
    }
}
