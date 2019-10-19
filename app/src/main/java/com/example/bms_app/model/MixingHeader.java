package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MixingHeader {

    @SerializedName("mixingHeaderList")
    @Expose
    private List<MixingHeaderList> mixingHeaderList = null;
    @SerializedName("errorMessage")
    @Expose
    private ErrorMessage errorMessage;

    public List<MixingHeaderList> getMixingHeaderList() {
        return mixingHeaderList;
    }

    public void setMixingHeaderList(List<MixingHeaderList> mixingHeaderList) {
        this.mixingHeaderList = mixingHeaderList;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "MixingHeader{" +
                "mixingHeaderList=" + mixingHeaderList +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
