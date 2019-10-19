package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductionDetail {
    @SerializedName("prodPlanHeader")
    @Expose
    private List<ProdPlanHeader> prodPlanHeader = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<ProdPlanHeader> getProdPlanHeader() {
        return prodPlanHeader;
    }

    public void setProdPlanHeader(List<ProdPlanHeader> prodPlanHeader) {
        this.prodPlanHeader = prodPlanHeader;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ProductionDetail{" +
                "prodPlanHeader=" + prodPlanHeader +
                ", info=" + info +
                '}';
    }
}
