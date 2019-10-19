package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FrItemStockConfigure {

    @SerializedName("settingId")
    @Expose
    private Integer settingId;
    @SerializedName("settingKey")
    @Expose
    private String settingKey;
    @SerializedName("settingValue")
    @Expose
    private Integer settingValue;

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public Integer getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(Integer settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public String toString() {
        return "FrItemStockConfigure{" +
                "settingId=" + settingId +
                ", settingKey='" + settingKey + '\'' +
                ", settingValue=" + settingValue +
                '}';
    }
}
