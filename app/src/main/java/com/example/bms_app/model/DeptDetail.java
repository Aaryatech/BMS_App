package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeptDetail {

    @SerializedName("sfPlanDetailForMixing")
    @Expose
    private List<SfPlanDetailForMixing> sfPlanDetailForMixing = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<SfPlanDetailForMixing> getSfPlanDetailForMixing() {
        return sfPlanDetailForMixing;
    }

    public void setSfPlanDetailForMixing(List<SfPlanDetailForMixing> sfPlanDetailForMixing) {
        this.sfPlanDetailForMixing = sfPlanDetailForMixing;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "DeptDetail{" +
                "sfPlanDetailForMixing=" + sfPlanDetailForMixing +
                ", info=" + info +
                '}';
    }
}
