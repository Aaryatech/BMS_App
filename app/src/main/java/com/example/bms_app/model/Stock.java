package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stock {
    @SerializedName("bmsStockId")
    @Expose
    private Integer bmsStockId;
    @SerializedName("bmsStockDate")
    @Expose
    private String bmsStockDate;
    @SerializedName("bmsStatus")
    @Expose
    private Integer bmsStatus;
    @SerializedName("rmType")
    @Expose
    private Integer rmType;
    @SerializedName("exInt")
    @Expose
    private Integer exInt;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exBoll")
    @Expose
    private Integer exBoll;
    @SerializedName("exBoll1")
    @Expose
    private Integer exBoll1;
    @SerializedName("exVarchar")
    @Expose
    private String exVarchar;
    @SerializedName("bmsStockDetailed")
    @Expose
    private Object bmsStockDetailed;

    public Integer getBmsStockId() {
        return bmsStockId;
    }

    public void setBmsStockId(Integer bmsStockId) {
        this.bmsStockId = bmsStockId;
    }

    public String getBmsStockDate() {
        return bmsStockDate;
    }

    public void setBmsStockDate(String bmsStockDate) {
        this.bmsStockDate = bmsStockDate;
    }

    public Integer getBmsStatus() {
        return bmsStatus;
    }

    public void setBmsStatus(Integer bmsStatus) {
        this.bmsStatus = bmsStatus;
    }

    public Integer getRmType() {
        return rmType;
    }

    public void setRmType(Integer rmType) {
        this.rmType = rmType;
    }

    public Integer getExInt() {
        return exInt;
    }

    public void setExInt(Integer exInt) {
        this.exInt = exInt;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExBoll() {
        return exBoll;
    }

    public void setExBoll(Integer exBoll) {
        this.exBoll = exBoll;
    }

    public Integer getExBoll1() {
        return exBoll1;
    }

    public void setExBoll1(Integer exBoll1) {
        this.exBoll1 = exBoll1;
    }

    public String getExVarchar() {
        return exVarchar;
    }

    public void setExVarchar(String exVarchar) {
        this.exVarchar = exVarchar;
    }

    public Object getBmsStockDetailed() {
        return bmsStockDetailed;
    }

    public void setBmsStockDetailed(Object bmsStockDetailed) {
        this.bmsStockDetailed = bmsStockDetailed;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "bmsStockId=" + bmsStockId +
                ", bmsStockDate='" + bmsStockDate + '\'' +
                ", bmsStatus=" + bmsStatus +
                ", rmType=" + rmType +
                ", exInt=" + exInt +
                ", exInt1=" + exInt1 +
                ", exBoll=" + exBoll +
                ", exBoll1=" + exBoll1 +
                ", exVarchar='" + exVarchar + '\'' +
                ", bmsStockDetailed=" + bmsStockDetailed +
                '}';
    }
}
