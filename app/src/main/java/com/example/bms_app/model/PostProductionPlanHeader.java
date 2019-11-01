package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostProductionPlanHeader {

    @SerializedName("productionHeaderId")
    @Expose
    private Integer productionHeaderId;
    @SerializedName("itemGrp1")
    @Expose
    private Integer itemGrp1;
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
    @SerializedName("isPlanned")
    @Expose
    private Integer isPlanned;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("postProductionPlanDetail")
    @Expose
    private List<PostProductionPlanDetail> postProductionPlanDetail = null;

    public Integer getProductionHeaderId() {
        return productionHeaderId;
    }

    public void setProductionHeaderId(Integer productionHeaderId) {
        this.productionHeaderId = productionHeaderId;
    }

    public Integer getItemGrp1() {
        return itemGrp1;
    }

    public void setItemGrp1(Integer itemGrp1) {
        this.itemGrp1 = itemGrp1;
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

    public Integer getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(Integer isPlanned) {
        this.isPlanned = isPlanned;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public List<PostProductionPlanDetail> getPostProductionPlanDetail() {
        return postProductionPlanDetail;
    }

    public void setPostProductionPlanDetail(List<PostProductionPlanDetail> postProductionPlanDetail) {
        this.postProductionPlanDetail = postProductionPlanDetail;
    }

    @Override
    public String toString() {
        return "PostProductionPlanHeader{" +
                "productionHeaderId=" + productionHeaderId +
                ", itemGrp1=" + itemGrp1 +
                ", timeSlot=" + timeSlot +
                ", productionBatch='" + productionBatch + '\'' +
                ", productionStatus='" + productionStatus + '\'' +
                ", productionDate='" + productionDate + '\'' +
                ", isMixing=" + isMixing +
                ", isBom=" + isBom +
                ", isPlanned=" + isPlanned +
                ", delStatus=" + delStatus +
                ", postProductionPlanDetail=" + postProductionPlanDetail +
                '}';
    }
}
