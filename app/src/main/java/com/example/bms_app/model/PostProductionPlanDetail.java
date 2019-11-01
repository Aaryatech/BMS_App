package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostProductionPlanDetail {

    @SerializedName("productionDetailId")
    @Expose
    private Integer productionDetailId;
    @SerializedName("productionHeaderId")
    @Expose
    private Integer productionHeaderId;
    @SerializedName("planQty")
    @Expose
    private Integer planQty;
    @SerializedName("orderQty")
    @Expose
    private Integer orderQty;
    @SerializedName("openingQty")
    @Expose
    private Integer openingQty;
    @SerializedName("rejectedQty")
    @Expose
    private Integer rejectedQty;
    @SerializedName("productionQty")
    @Expose
    private Integer productionQty;
    @SerializedName("itemId")
    @Expose
    private Integer itemId;
    @SerializedName("productionBatch")
    @Expose
    private String productionBatch;
    @SerializedName("productionDate")
    @Expose
    private String productionDate;
    @SerializedName("int4")
    @Expose
    private Integer int4;

    public Integer getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(Integer productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public Integer getProductionHeaderId() {
        return productionHeaderId;
    }

    public void setProductionHeaderId(Integer productionHeaderId) {
        this.productionHeaderId = productionHeaderId;
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

    public Integer getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(Integer openingQty) {
        this.openingQty = openingQty;
    }

    public Integer getRejectedQty() {
        return rejectedQty;
    }

    public void setRejectedQty(Integer rejectedQty) {
        this.rejectedQty = rejectedQty;
    }

    public Integer getProductionQty() {
        return productionQty;
    }

    public void setProductionQty(Integer productionQty) {
        this.productionQty = productionQty;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getInt4() {
        return int4;
    }

    public void setInt4(Integer int4) {
        this.int4 = int4;
    }

    @Override
    public String toString() {
        return "PostProductionPlanDetail{" +
                "productionDetailId=" + productionDetailId +
                ", productionHeaderId=" + productionHeaderId +
                ", planQty=" + planQty +
                ", orderQty=" + orderQty +
                ", openingQty=" + openingQty +
                ", rejectedQty=" + rejectedQty +
                ", productionQty=" + productionQty +
                ", itemId=" + itemId +
                ", productionBatch='" + productionBatch + '\'' +
                ", productionDate='" + productionDate + '\'' +
                ", int4=" + int4 +
                '}';
    }
}
