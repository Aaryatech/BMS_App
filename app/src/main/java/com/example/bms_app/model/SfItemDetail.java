package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SfItemDetail {

    @SerializedName("sfDid")
    @Expose
    private Integer sfDid;
    @SerializedName("sfId")
    @Expose
    private Integer sfId;
    @SerializedName("rmType")
    @Expose
    private Integer rmType;
    @SerializedName("rmId")
    @Expose
    private Integer rmId;
    @SerializedName("rmName")
    @Expose
    private String rmName;
    @SerializedName("rmQty")
    @Expose
    private float rmQty;
    @SerializedName("rmUnit")
    @Expose
    private Integer rmUnit;
    @SerializedName("rmWeight")
    @Expose
    private Integer rmWeight;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;

    public Integer getSfDid() {
        return sfDid;
    }

    public void setSfDid(Integer sfDid) {
        this.sfDid = sfDid;
    }

    public Integer getSfId() {
        return sfId;
    }

    public void setSfId(Integer sfId) {
        this.sfId = sfId;
    }

    public Integer getRmType() {
        return rmType;
    }

    public void setRmType(Integer rmType) {
        this.rmType = rmType;
    }

    public Integer getRmId() {
        return rmId;
    }

    public void setRmId(Integer rmId) {
        this.rmId = rmId;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public float getRmQty() {
        return rmQty;
    }

    public void setRmQty(float rmQty) {
        this.rmQty = rmQty;
    }

    public Integer getRmUnit() {
        return rmUnit;
    }

    public void setRmUnit(Integer rmUnit) {
        this.rmUnit = rmUnit;
    }

    public Integer getRmWeight() {
        return rmWeight;
    }

    public void setRmWeight(Integer rmWeight) {
        this.rmWeight = rmWeight;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "SfItemDetail{" +
                "sfDid=" + sfDid +
                ", sfId=" + sfId +
                ", rmType=" + rmType +
                ", rmId=" + rmId +
                ", rmName='" + rmName + '\'' +
                ", rmQty=" + rmQty +
                ", rmUnit=" + rmUnit +
                ", rmWeight=" + rmWeight +
                ", delStatus=" + delStatus +
                '}';
    }
}
