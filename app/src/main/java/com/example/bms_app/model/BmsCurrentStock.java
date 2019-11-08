package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BmsCurrentStock {
    @SerializedName("rmId")
    @Expose
    private Integer rmId;
    @SerializedName("rmName")
    @Expose
    private String rmName;
    @SerializedName("rmUomId")
    @Expose
    private Integer rmUomId;
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
    @SerializedName("mixingReturnQty")
    @Expose
    private Integer mixingReturnQty;
    @SerializedName("storeIssueQty")
    @Expose
    private Integer storeIssueQty;
    @SerializedName("storeRejectedQty")
    @Expose
    private Integer storeRejectedQty;
    @SerializedName("bmsOpeningStock")
    @Expose
    private float bmsOpeningStock;
    @SerializedName("bmsClosingStock")
    @Expose
    private float bmsClosingStock;

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

    public Integer getRmUomId() {
        return rmUomId;
    }

    public void setRmUomId(Integer rmUomId) {
        this.rmUomId = rmUomId;
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

    public Integer getMixingReturnQty() {
        return mixingReturnQty;
    }

    public void setMixingReturnQty(Integer mixingReturnQty) {
        this.mixingReturnQty = mixingReturnQty;
    }

    public Integer getStoreIssueQty() {
        return storeIssueQty;
    }

    public void setStoreIssueQty(Integer storeIssueQty) {
        this.storeIssueQty = storeIssueQty;
    }

    public Integer getStoreRejectedQty() {
        return storeRejectedQty;
    }

    public void setStoreRejectedQty(Integer storeRejectedQty) {
        this.storeRejectedQty = storeRejectedQty;
    }

    public float getBmsOpeningStock() {
        return bmsOpeningStock;
    }

    public void setBmsOpeningStock(float bmsOpeningStock) {
        this.bmsOpeningStock = bmsOpeningStock;
    }

    public float getBmsClosingStock() {
        return bmsClosingStock;
    }

    public void setBmsClosingStock(float bmsClosingStock) {
        this.bmsClosingStock = bmsClosingStock;
    }

    @Override
    public String toString() {
        return "BmsCurrentStock{" +
                "rmId=" + rmId +
                ", rmName='" + rmName + '\'' +
                ", rmUomId=" + rmUomId +
                ", prodIssueQty=" + prodIssueQty +
                ", prodRejectedQty=" + prodRejectedQty +
                ", prodReturnQty=" + prodReturnQty +
                ", mixingIssueQty=" + mixingIssueQty +
                ", mixingRejectedQty=" + mixingRejectedQty +
                ", mixingReturnQty=" + mixingReturnQty +
                ", storeIssueQty=" + storeIssueQty +
                ", storeRejectedQty=" + storeRejectedQty +
                ", bmsOpeningStock=" + bmsOpeningStock +
                ", bmsClosingStock=" + bmsClosingStock +
                '}';
    }
}
