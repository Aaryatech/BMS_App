package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockDetailSf {
    @SerializedName("currentBmsSFStock")
    @Expose
    private List<CurrentBmsSFStock> currentBmsSFStock = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<CurrentBmsSFStock> getCurrentBmsSFStock() {
        return currentBmsSFStock;
    }

    public void setCurrentBmsSFStock(List<CurrentBmsSFStock> currentBmsSFStock) {
        this.currentBmsSFStock = currentBmsSFStock;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "StockList{" +
                "currentBmsSFStock=" + currentBmsSFStock +
                ", info=" + info +
                '}';
    }
}
