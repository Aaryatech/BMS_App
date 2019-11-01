package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SfPlanDetailForMixing {

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
    private float rmQty;
    @SerializedName("noPiecesPerItem")
    @Expose
    private float noPiecesPerItem;
    @SerializedName("rmName")
    @Expose
    private String rmName;
    @SerializedName("planQty")
    @Expose
    private float planQty;
    @SerializedName("singleCut")
    @Expose
    private float singleCut;
    @SerializedName("doubleCut")
    @Expose
    private float doubleCut;
    @SerializedName("total")
    @Expose
    private float total;

    private float editTotal;
    private float prodQty;

    private Boolean isChecked;

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

    public float getRmQty() {
        return rmQty;
    }

    public void setRmQty(float rmQty) {
        this.rmQty = rmQty;
    }

    public float getNoPiecesPerItem() {
        return noPiecesPerItem;
    }

    public void setNoPiecesPerItem(float noPiecesPerItem) {
        this.noPiecesPerItem = noPiecesPerItem;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public float getPlanQty() {
        return planQty;
    }

    public void setPlanQty(float planQty) {
        this.planQty = planQty;
    }

    public float getSingleCut() {
        return singleCut;
    }

    public void setSingleCut(float singleCut) {
        this.singleCut = singleCut;
    }

    public float getDoubleCut() {
        return doubleCut;
    }

    public void setDoubleCut(float doubleCut) {
        this.doubleCut = doubleCut;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getEditTotal() {
        return editTotal;
    }

    public void setEditTotal(float editTotal) {
        this.editTotal = editTotal;
    }

    public float getProdQty() {
        return prodQty;
    }

    public void setProdQty(float prodQty) {
        this.prodQty = prodQty;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "SfPlanDetailForMixing{" +
                "itemDetailId=" + itemDetailId +
                ", itemId=" + itemId +
                ", rmType=" + rmType +
                ", rmId=" + rmId +
                ", uom='" + uom + '\'' +
                ", rmQty=" + rmQty +
                ", noPiecesPerItem=" + noPiecesPerItem +
                ", rmName='" + rmName + '\'' +
                ", planQty=" + planQty +
                ", singleCut=" + singleCut +
                ", doubleCut=" + doubleCut +
                ", total=" + total +
                ", editTotal=" + editTotal +
                ", prodQty=" + prodQty +
                ", isChecked=" + isChecked +
                '}';
    }
}
