package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MixingHeaderList {

    @SerializedName("mixId")
    @Expose
    private Integer mixId;
    @SerializedName("mixDate")
    @Expose
    private String mixDate;
    @SerializedName("productionBatch")
    @Expose
    private String productionBatch;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("timeSlot")
    @Expose
    private Integer timeSlot;
    @SerializedName("isBom")
    @Expose
    private Integer isBom;
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
    @SerializedName("mixingDetailed")
    @Expose
    private Object mixingDetailed;
    @SerializedName("productionId")
    @Expose
    private Integer productionId;
    int visibleStatus;

    public Integer getMixId() {
        return mixId;
    }

    public void setMixId(Integer mixId) {
        this.mixId = mixId;
    }

    public String getMixDate() {
        return mixDate;
    }

    public void setMixDate(String mixDate) {
        this.mixDate = mixDate;
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Integer timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getIsBom() {
        return isBom;
    }

    public void setIsBom(Integer isBom) {
        this.isBom = isBom;
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

    public Object getMixingDetailed() {
        return mixingDetailed;
    }

    public void setMixingDetailed(Object mixingDetailed) {
        this.mixingDetailed = mixingDetailed;
    }

    public Integer getProductionId() {
        return productionId;
    }

    public void setProductionId(Integer productionId) {
        this.productionId = productionId;
    }


    public int getVisibleStatus() {
        return visibleStatus;
    }

    public void setVisibleStatus(int visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

    @Override
    public String toString() {
        return "MixingHeaderList{" +
                "mixId=" + mixId +
                ", mixDate='" + mixDate + '\'' +
                ", productionBatch='" + productionBatch + '\'' +
                ", status=" + status +
                ", delStatus=" + delStatus +
                ", timeSlot=" + timeSlot +
                ", isBom=" + isBom +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVarchar1='" + exVarchar1 + '\'' +
                ", exVarchar2='" + exVarchar2 + '\'' +
                ", exVarchar3='" + exVarchar3 + '\'' +
                ", exBool1=" + exBool1 +
                ", mixingDetailed=" + mixingDetailed +
                ", productionId=" + productionId +
                ", visibleStatus=" + visibleStatus +
                '}';
    }
}
