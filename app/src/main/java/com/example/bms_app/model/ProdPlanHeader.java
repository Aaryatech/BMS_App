package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProdPlanHeader {

    @SerializedName("productionHeaderId")
    @Expose
    private Integer productionHeaderId;
    @SerializedName("catId")
    @Expose
    private Integer catId;
    @SerializedName("timeSlot")
    @Expose
    private Integer timeSlot;
    @SerializedName("productionBatch")
    @Expose
    private String productionBatch;
    @SerializedName("productionStatus")
    @Expose
    private String productionStatus;
    @SerializedName("productionDate")
    @Expose
    private String productionDate;
    @SerializedName("isMixing")
    @Expose
    private Integer isMixing;
    @SerializedName("isBom")
    @Expose
    private Integer isBom;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("catName")
    @Expose
    private String catName;
    @SerializedName("isPlanned")
    @Expose
    private Integer isPlanned;
    @SerializedName("isStoreBom")
    @Expose
    private Integer isStoreBom;

    private Boolean isChecked;

    public Integer getProductionHeaderId() {
        return productionHeaderId;
    }

    public void setProductionHeaderId(Integer productionHeaderId) {
        this.productionHeaderId = productionHeaderId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Integer timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch;
    }

    public String getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(String productionStatus) {
        this.productionStatus = productionStatus;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getIsMixing() {
        return isMixing;
    }

    public void setIsMixing(Integer isMixing) {
        this.isMixing = isMixing;
    }

    public Integer getIsBom() {
        return isBom;
    }

    public void setIsBom(Integer isBom) {
        this.isBom = isBom;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Integer getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(Integer isPlanned) {
        this.isPlanned = isPlanned;
    }

    public Integer getIsStoreBom() {
        return isStoreBom;
    }

    public void setIsStoreBom(Integer isStoreBom) {
        this.isStoreBom = isStoreBom;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ProdPlanHeader{" +
                "productionHeaderId=" + productionHeaderId +
                ", catId=" + catId +
                ", timeSlot=" + timeSlot +
                ", productionBatch='" + productionBatch + '\'' +
                ", productionStatus='" + productionStatus + '\'' +
                ", productionDate='" + productionDate + '\'' +
                ", isMixing=" + isMixing +
                ", isBom=" + isBom +
                ", delStatus=" + delStatus +
                ", catName='" + catName + '\'' +
                ", isPlanned=" + isPlanned +
                ", isStoreBom=" + isStoreBom +
                ", isChecked=" + isChecked +
                '}';
    }
}
