package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @Expose
    private Integer miniCatId;
    @SerializedName("miniCatName")
    @Expose
    private String miniCatName;
    @SerializedName("subCatId")
    @Expose
    private Integer subCatId;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("subCatName")
    @Expose
    private String subCatName;
    @SerializedName("catName")
    @Expose
    private String catName;

    public Integer getMiniCatId() {
        return miniCatId;
    }

    public void setMiniCatId(Integer miniCatId) {
        this.miniCatId = miniCatId;
    }

    public String getMiniCatName() {
        return miniCatName;
    }

    public void setMiniCatName(String miniCatName) {
        this.miniCatName = miniCatName;
    }

    public Integer getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(Integer subCatId) {
        this.subCatId = subCatId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "miniCatId=" + miniCatId +
                ", miniCatName='" + miniCatName + '\'' +
                ", subCatId=" + subCatId +
                ", delStatus=" + delStatus +
                ", subCatName='" + subCatName + '\'' +
                ", catName='" + catName + '\'' +
                '}';
    }
}
