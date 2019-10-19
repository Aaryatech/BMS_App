package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempMixItemDetail {

    @SerializedName("tempId")
    @Expose
    private Integer tempId;
    @SerializedName("sfId")
    @Expose
    private Integer sfId;
    @SerializedName("rmId")
    @Expose
    private Integer rmId;
    @SerializedName("rmName")
    @Expose
    private String rmName;
    @SerializedName("rmType")
    @Expose
    private Integer rmType;
    @SerializedName("uom")
    @Expose
    private String uom;
    @SerializedName("mulFactor")
    @Expose
    private Double mulFactor;
    @SerializedName("total")
    @Expose
    private float total;

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public Integer getSfId() {
        return sfId;
    }

    public void setSfId(Integer sfId) {
        this.sfId = sfId;
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

    public Integer getRmType() {
        return rmType;
    }

    public void setRmType(Integer rmType) {
        this.rmType = rmType;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getMulFactor() {
        return mulFactor;
    }

    public void setMulFactor(Double mulFactor) {
        this.mulFactor = mulFactor;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "TempMixItemDetail{" +
                "tempId=" + tempId +
                ", sfId=" + sfId +
                ", rmId=" + rmId +
                ", rmName='" + rmName + '\'' +
                ", rmType=" + rmType +
                ", uom='" + uom + '\'' +
                ", mulFactor=" + mulFactor +
                ", total=" + total +
                '}';
    }
}
