package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProdMixingReqP1 {

    @SerializedName("itemDetailId")
    @Expose
    private Integer itemDetailId;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("rmType")
    @Expose
    private Integer rmType;
    @SerializedName("rmId")
    @Expose
    private Integer rmId;
    @SerializedName("uom")
    @Expose
    private String uom;
    @SerializedName("rmQty")
    @Expose
    private Integer rmQty;
    @SerializedName("noPiecesPerItem")
    @Expose
    private Integer noPiecesPerItem;
    @SerializedName("rmName")
    @Expose
    private String rmName;
    @SerializedName("planQty")
    @Expose
    private Integer planQty;
    @SerializedName("orderQty")
    @Expose
    private Integer orderQty;
    @SerializedName("mulFactor")
    @Expose
    private float mulFactor;
    @SerializedName("total")
    @Expose
    private float total;

    private float prevtotal;

    public Integer getItemDetailId() {
        return itemDetailId;
    }

    public void setItemDetailId(Integer itemDetailId) {
        this.itemDetailId = itemDetailId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Integer getRmQty() {
        return rmQty;
    }

    public void setRmQty(Integer rmQty) {
        this.rmQty = rmQty;
    }

    public Integer getNoPiecesPerItem() {
        return noPiecesPerItem;
    }

    public void setNoPiecesPerItem(Integer noPiecesPerItem) {
        this.noPiecesPerItem = noPiecesPerItem;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public Integer getPlanQty() {
        return planQty;
    }

    public void setPlanQty(Integer planQty) {
        this.planQty = planQty;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public float getMulFactor() {
        return mulFactor;
    }

    public void setMulFactor(float mulFactor) {
        this.mulFactor = mulFactor;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getPrevtotal() {
        return prevtotal;
    }

    public void setPrevtotal(float prevtotal) {
        this.prevtotal = prevtotal;
    }

    @Override
    public String toString() {
        return "ProdMixingReqP1{" +
                "itemDetailId=" + itemDetailId +
                ", itemId=" + itemId +
                ", rmType=" + rmType +
                ", rmId=" + rmId +
                ", uom='" + uom + '\'' +
                ", rmQty=" + rmQty +
                ", noPiecesPerItem=" + noPiecesPerItem +
                ", rmName='" + rmName + '\'' +
                ", planQty=" + planQty +
                ", orderQty=" + orderQty +
                ", mulFactor=" + mulFactor +
                ", total=" + total +
                ", prevtotal=" + prevtotal +
                '}';
    }
}
