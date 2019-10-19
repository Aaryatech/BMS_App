package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configure {
    @SerializedName("frItemStockConfigure")
    @Expose
    private List<FrItemStockConfigure> frItemStockConfigure = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<FrItemStockConfigure> getFrItemStockConfigure() {
        return frItemStockConfigure;
    }

    public void setFrItemStockConfigure(List<FrItemStockConfigure> frItemStockConfigure) {
        this.frItemStockConfigure = frItemStockConfigure;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Configure{" +
                "frItemStockConfigure=" + frItemStockConfigure +
                ", info=" + info +
                '}';
    }
}
