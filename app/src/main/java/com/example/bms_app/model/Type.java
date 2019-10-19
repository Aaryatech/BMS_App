package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Type {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sfTypeName")
    @Expose
    private String sfTypeName;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSfTypeName() {
        return sfTypeName;
    }

    public void setSfTypeName(String sfTypeName) {
        this.sfTypeName = sfTypeName;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", sfTypeName='" + sfTypeName + '\'' +
                ", delStatus=" + delStatus +
                '}';
    }
}
