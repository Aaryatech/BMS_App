package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentBmsSFStock {
    @SerializedName("sfId")
    @Expose
    private Integer sfId;
    @SerializedName("sfName")
    @Expose
    private String sfName;
    @SerializedName("sfUomId")
    @Expose
    private Integer sfUomId;
    @SerializedName("prodIssueQty")
    @Expose
    private float prodIssueQty;
    @SerializedName("prodRejectedQty")
    @Expose
    private Integer prodRejectedQty;
    @SerializedName("prodReturnQty")
    @Expose
    private Integer prodReturnQty;
    @SerializedName("mixingIssueQty")
    @Expose
    private float mixingIssueQty;
    @SerializedName("mixingRejectedQty")
    @Expose
    private Integer mixingRejectedQty;
    @SerializedName("bmsClosingStock")
    @Expose
    private float bmsClosingStock;
    @SerializedName("bmsOpeningStock")
    @Expose
    private float bmsOpeningStock;

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

    public Integer getSfUomId() {
        return sfUomId;
    }

    public void setSfUomId(Integer sfUomId) {
        this.sfUomId = sfUomId;
    }

    public float getProdIssueQty() {
        return prodIssueQty;
    }

    public void setProdIssueQty(float prodIssueQty) {
        this.prodIssueQty = prodIssueQty;
    }

    public Integer getProdRejectedQty() {
        return prodRejectedQty;
    }

    public void setProdRejectedQty(Integer prodRejectedQty) {
        this.prodRejectedQty = prodRejectedQty;
    }

    public Integer getProdReturnQty() {
        return prodReturnQty;
    }

    public void setProdReturnQty(Integer prodReturnQty) {
        this.prodReturnQty = prodReturnQty;
    }

    public float getMixingIssueQty() {
        return mixingIssueQty;
    }

    public void setMixingIssueQty(float mixingIssueQty) {
        this.mixingIssueQty = mixingIssueQty;
    }

    public Integer getMixingRejectedQty() {
        return mixingRejectedQty;
    }

    public void setMixingRejectedQty(Integer mixingRejectedQty) {
        this.mixingRejectedQty = mixingRejectedQty;
    }

    public float getBmsClosingStock() {
        return bmsClosingStock;
    }

    public void setBmsClosingStock(float bmsClosingStock) {
        this.bmsClosingStock = bmsClosingStock;
    }

    public float getBmsOpeningStock() {
        return bmsOpeningStock;
    }

    public void setBmsOpeningStock(float bmsOpeningStock) {
        this.bmsOpeningStock = bmsOpeningStock;
    }

    @Override
    public String toString() {
        return "CurrentBmsSFStock{" +
                "sfId=" + sfId +
                ", sfName='" + sfName + '\'' +
                ", sfUomId=" + sfUomId +
                ", prodIssueQty=" + prodIssueQty +
                ", prodRejectedQty=" + prodRejectedQty +
                ", prodReturnQty=" + prodReturnQty +
                ", mixingIssueQty=" + mixingIssueQty +
                ", mixingRejectedQty=" + mixingRejectedQty +
                ", bmsClosingStock=" + bmsClosingStock +
                ", bmsOpeningStock=" + bmsOpeningStock +
                '}';
    }
}
