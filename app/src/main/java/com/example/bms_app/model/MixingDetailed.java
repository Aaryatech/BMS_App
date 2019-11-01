package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MixingDetailed {
    @SerializedName("mixing_detailId")
    @Expose
    private Integer mixingDetailId;
    @SerializedName("mixingId")
    @Expose
    private Integer mixingId;
    @SerializedName("sfId")
    @Expose
    private Integer sfId;
    @SerializedName("sfName")
    @Expose
    private String sfName;
    @SerializedName("receivedQty")
    @Expose
    private float receivedQty;
    @SerializedName("productionQty")
    @Expose
    private float productionQty;
    @SerializedName("mixingDate")
    @Expose
    private String mixingDate;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exInt2")
    @Expose
    private Integer exInt2;
    @SerializedName("exInt3")
    @Expose
    private Integer exInt3;
    @SerializedName("exVarchar1")
    @Expose
    private String exVarchar1;
    @SerializedName("exVarchar2")
    @Expose
    private String exVarchar2;
    @SerializedName("exVarchar3")
    @Expose
    private String exVarchar3;
    @SerializedName("exBool1")
    @Expose
    private Integer exBool1;
    @SerializedName("uom")
    @Expose
    private String uom;
    @SerializedName("rejectedQty")
    @Expose
    private Integer rejectedQty;
    @SerializedName("originalQty")
    @Expose
    private Double originalQty;
    @SerializedName("autoOrderQty")
    @Expose
    private Double autoOrderQty;

    public Integer getMixingDetailId() {
        return mixingDetailId;
    }

    public void setMixingDetailId(Integer mixingDetailId) {
        this.mixingDetailId = mixingDetailId;
    }

    public Integer getMixingId() {
        return mixingId;
    }

    public void setMixingId(Integer mixingId) {
        this.mixingId = mixingId;
    }

    public Integer getSfId() {
        return sfId;
    }

    public void setSfId(Integer sfId) {
        this.sfId = sfId;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public float getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(float receivedQty) {
        this.receivedQty = receivedQty;
    }

    public float getProductionQty() {
        return productionQty;
    }

    public void setProductionQty(float productionQty) {
        this.productionQty = productionQty;
    }

    public String getMixingDate() {
        return mixingDate;
    }

    public void setMixingDate(String mixingDate) {
        this.mixingDate = mixingDate;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public Integer getExInt3() {
        return exInt3;
    }

    public void setExInt3(Integer exInt3) {
        this.exInt3 = exInt3;
    }

    public String getExVarchar1() {
        return exVarchar1;
    }

    public void setExVarchar1(String exVarchar1) {
        this.exVarchar1 = exVarchar1;
    }

    public String getExVarchar2() {
        return exVarchar2;
    }

    public void setExVarchar2(String exVarchar2) {
        this.exVarchar2 = exVarchar2;
    }

    public String getExVarchar3() {
        return exVarchar3;
    }

    public void setExVarchar3(String exVarchar3) {
        this.exVarchar3 = exVarchar3;
    }

    public Integer getExBool1() {
        return exBool1;
    }

    public void setExBool1(Integer exBool1) {
        this.exBool1 = exBool1;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Integer getRejectedQty() {
        return rejectedQty;
    }

    public void setRejectedQty(Integer rejectedQty) {
        this.rejectedQty = rejectedQty;
    }

    public Double getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(Double originalQty) {
        this.originalQty = originalQty;
    }

    public Double getAutoOrderQty() {
        return autoOrderQty;
    }

    public void setAutoOrderQty(Double autoOrderQty) {
        this.autoOrderQty = autoOrderQty;
    }


    @Override
    public String toString() {
        return "MixingDetailed{" +
                "mixingDetailId=" + mixingDetailId +
                ", mixingId=" + mixingId +
                ", sfId=" + sfId +
                ", sfName='" + sfName + '\'' +
                ", receivedQty=" + receivedQty +
                ", productionQty=" + productionQty +
                ", mixingDate='" + mixingDate + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVarchar1='" + exVarchar1 + '\'' +
                ", exVarchar2='" + exVarchar2 + '\'' +
                ", exVarchar3='" + exVarchar3 + '\'' +
                ", exBool1=" + exBool1 +
                ", uom='" + uom + '\'' +
                ", rejectedQty=" + rejectedQty +
                ", originalQty=" + originalQty +
                ", autoOrderQty=" + autoOrderQty +
                '}';
    }
}
