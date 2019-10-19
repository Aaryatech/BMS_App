package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MixingDetail {

    @SerializedName("prodMixingReqP1")
    @Expose
    private List<ProdMixingReqP1> prodMixingReqP1 = null;

    public List<ProdMixingReqP1> getProdMixingReqP1() {
        return prodMixingReqP1;
    }

    public void setProdMixingReqP1(List<ProdMixingReqP1> prodMixingReqP1) {
        this.prodMixingReqP1 = prodMixingReqP1;
    }

    @Override
    public String toString() {
        return "MixingDetail{" +
                "prodMixingReqP1=" + prodMixingReqP1 +
                '}';
    }
}
