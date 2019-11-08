package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockDetail {
    @SerializedName("bmsCurrentStock")
    @Expose
    private List<BmsCurrentStock> bmsCurrentStock = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<BmsCurrentStock> getBmsCurrentStock() {
        return bmsCurrentStock;
    }

    public void setBmsCurrentStock(List<BmsCurrentStock> bmsCurrentStock) {
        this.bmsCurrentStock = bmsCurrentStock;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "StockDetail{" +
                "bmsCurrentStock=" + bmsCurrentStock +
                ", info=" + info +
                '}';
    }
}
