package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Production {

    @SerializedName("sfName")
    @Expose
    private String sfName;
    @SerializedName("sfId")
    @Expose
    private Integer sfId;
    @SerializedName("sfType")
    @Expose
    private String sfType;
    @SerializedName("sfTypeName")
    @Expose
    private String sfTypeName;
    @SerializedName("sfUomId")
    @Expose
    private Integer sfUomId;
    @SerializedName("sfWeight")
    @Expose
    private Integer sfWeight;
    @SerializedName("stockQty")
    @Expose
    private Integer stockQty;
    @SerializedName("minLevelQty")
    @Expose
    private Integer minLevelQty;
    @SerializedName("maxLevelQty")
    @Expose
    private Integer maxLevelQty;
    @SerializedName("reorderLevelQty")
    @Expose
    private Integer reorderLevelQty;
    @SerializedName("mulFactor")
    @Expose
    private Integer mulFactor;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("int1")
    @Expose
    private Integer int1;
    @SerializedName("int2")
    @Expose
    private Integer int2;
    @SerializedName("varchar1")
    @Expose
    private Object varchar1;
    @SerializedName("varchar2")
    @Expose
    private Object varchar2;
    @SerializedName("bool1")
    @Expose
    private Integer bool1;

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public Integer getSfId() {
        return sfId;
    }

    public void setSfId(Integer sfId) {
        this.sfId = sfId;
    }

    public String getSfType() {
        return sfType;
    }

    public void setSfType(String sfType) {
        this.sfType = sfType;
    }

    public String getSfTypeName() {
        return sfTypeName;
    }

    public void setSfTypeName(String sfTypeName) {
        this.sfTypeName = sfTypeName;
    }

    public Integer getSfUomId() {
        return sfUomId;
    }

    public void setSfUomId(Integer sfUomId) {
        this.sfUomId = sfUomId;
    }

    public Integer getSfWeight() {
        return sfWeight;
    }

    public void setSfWeight(Integer sfWeight) {
        this.sfWeight = sfWeight;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public Integer getMinLevelQty() {
        return minLevelQty;
    }

    public void setMinLevelQty(Integer minLevelQty) {
        this.minLevelQty = minLevelQty;
    }

    public Integer getMaxLevelQty() {
        return maxLevelQty;
    }

    public void setMaxLevelQty(Integer maxLevelQty) {
        this.maxLevelQty = maxLevelQty;
    }

    public Integer getReorderLevelQty() {
        return reorderLevelQty;
    }

    public void setReorderLevelQty(Integer reorderLevelQty) {
        this.reorderLevelQty = reorderLevelQty;
    }

    public Integer getMulFactor() {
        return mulFactor;
    }

    public void setMulFactor(Integer mulFactor) {
        this.mulFactor = mulFactor;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getInt1() {
        return int1;
    }

    public void setInt1(Integer int1) {
        this.int1 = int1;
    }

    public Integer getInt2() {
        return int2;
    }

    public void setInt2(Integer int2) {
        this.int2 = int2;
    }

    public Object getVarchar1() {
        return varchar1;
    }

    public void setVarchar1(Object varchar1) {
        this.varchar1 = varchar1;
    }

    public Object getVarchar2() {
        return varchar2;
    }

    public void setVarchar2(Object varchar2) {
        this.varchar2 = varchar2;
    }

    public Integer getBool1() {
        return bool1;
    }

    public void setBool1(Integer bool1) {
        this.bool1 = bool1;
    }

    @Override
    public String toString() {
        return "Production{" +
                "sfName='" + sfName + '\'' +
                ", sfId=" + sfId +
                ", sfType='" + sfType + '\'' +
                ", sfTypeName='" + sfTypeName + '\'' +
                ", sfUomId=" + sfUomId +
                ", sfWeight=" + sfWeight +
                ", stockQty=" + stockQty +
                ", minLevelQty=" + minLevelQty +
                ", maxLevelQty=" + maxLevelQty +
                ", reorderLevelQty=" + reorderLevelQty +
                ", mulFactor=" + mulFactor +
                ", delStatus=" + delStatus +
                ", int1=" + int1 +
                ", int2=" + int2 +
                ", varchar1=" + varchar1 +
                ", varchar2=" + varchar2 +
                ", bool1=" + bool1 +
                '}';
    }
}
