package com.example.bms_app.model;

public class MixingDetailedSave {

    private int mixing_detailId;
    private int mixingId;
    private int sfId;
    private String sfName;
    private float receivedQty;
    private float productionQty;
    private String mixingDate;
    private int exInt1;
    private int exInt2;
    private int exInt3;
    private String exVarchar1;
    private String exVarchar2;
    private String exVarchar3;
    private int exBool1;
    private String uom;
    private float rejectedQty;
    float originalQty;
    float autoOrderQty;

    public MixingDetailedSave(int mixing_detailId, int mixingId, int sfId, String sfName, float receivedQty, float productionQty, String mixingDate, int exInt1, int exInt2, int exInt3, String exVarchar1, String exVarchar2, String exVarchar3, int exBool1, String uom, float rejectedQty, float originalQty, float autoOrderQty) {
        this.mixing_detailId = mixing_detailId;
        this.mixingId = mixingId;
        this.sfId = sfId;
        this.sfName = sfName;
        this.receivedQty = receivedQty;
        this.productionQty = productionQty;
        this.mixingDate = mixingDate;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVarchar1 = exVarchar1;
        this.exVarchar2 = exVarchar2;
        this.exVarchar3 = exVarchar3;
        this.exBool1 = exBool1;
        this.uom = uom;
        this.rejectedQty = rejectedQty;
        this.originalQty = originalQty;
        this.autoOrderQty = autoOrderQty;
    }

    public int getMixing_detailId() {
        return mixing_detailId;
    }

    public void setMixing_detailId(int mixing_detailId) {
        this.mixing_detailId = mixing_detailId;
    }

    public int getMixingId() {
        return mixingId;
    }

    public void setMixingId(int mixingId) {
        this.mixingId = mixingId;
    }

    public int getSfId() {
        return sfId;
    }

    public void setSfId(int sfId) {
        this.sfId = sfId;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public float getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(float receivedQty) {
        this.receivedQty = receivedQty;
    }

    public float getProductionQty() {
        return productionQty;
    }

    public void setProductionQty(float productionQty) {
        this.productionQty = productionQty;
    }

    public String getMixingDate() {
        return mixingDate;
    }

    public void setMixingDate(String mixingDate) {
        this.mixingDate = mixingDate;
    }

    public int getExInt1() {
        return exInt1;
    }

    public void setExInt1(int exInt1) {
        this.exInt1 = exInt1;
    }

    public int getExInt2() {
        return exInt2;
    }

    public void setExInt2(int exInt2) {
        this.exInt2 = exInt2;
    }

    public int getExInt3() {
        return exInt3;
    }

    public void setExInt3(int exInt3) {
        this.exInt3 = exInt3;
    }

    public String getExVarchar1() {
        return exVarchar1;
    }

    public void setExVarchar1(String exVarchar1) {
        this.exVarchar1 = exVarchar1;
    }

    public String getExVarchar2() {
        return exVarchar2;
    }

    public void setExVarchar2(String exVarchar2) {
        this.exVarchar2 = exVarchar2;
    }

    public String getExVarchar3() {
        return exVarchar3;
    }

    public void setExVarchar3(String exVarchar3) {
        this.exVarchar3 = exVarchar3;
    }

    public int getExBool1() {
        return exBool1;
    }

    public void setExBool1(int exBool1) {
        this.exBool1 = exBool1;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public float getRejectedQty() {
        return rejectedQty;
    }

    public void setRejectedQty(float rejectedQty) {
        this.rejectedQty = rejectedQty;
    }

    public float getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(float originalQty) {
        this.originalQty = originalQty;
    }

    public float getAutoOrderQty() {
        return autoOrderQty;
    }

    public void setAutoOrderQty(float autoOrderQty) {
        this.autoOrderQty = autoOrderQty;
    }

    @Override
    public String toString() {
        return "MixingDetailedSave{" +
                "mixing_detailId=" + mixing_detailId +
                ", mixingId=" + mixingId +
                ", sfId=" + sfId +
                ", sfName='" + sfName + '\'' +
                ", receivedQty=" + receivedQty +
                ", productionQty=" + productionQty +
                ", mixingDate='" + mixingDate + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVarchar1='" + exVarchar1 + '\'' +
                ", exVarchar2='" + exVarchar2 + '\'' +
                ", exVarchar3='" + exVarchar3 + '\'' +
                ", exBool1=" + exBool1 +
                ", uom='" + uom + '\'' +
                ", rejectedQty=" + rejectedQty +
                ", originalQty=" + originalQty +
                ", autoOrderQty=" + autoOrderQty +
                '}';
    }
}
