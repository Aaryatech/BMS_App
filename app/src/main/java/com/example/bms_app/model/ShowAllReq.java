package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowAllReq {

    @SerializedName("billOfMaterialHeader")
    @Expose
    private List<BillOfAllMaterialHeader> billOfAllMaterialHeaders = null;
    @SerializedName("errorMessage")
    @Expose
    private ErrorMessage errorMessage;

    public List<BillOfAllMaterialHeader> getBillOfAllMaterialHeader() {
        return billOfAllMaterialHeaders;
    }

    public void setBillOfAllMaterialHeader(List<BillOfAllMaterialHeader> billOfMaterialHeader) {
        this.billOfAllMaterialHeaders = billOfMaterialHeader;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ShowAllReq{" +
                "billOfAllMaterialHeaders=" + billOfAllMaterialHeaders +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
