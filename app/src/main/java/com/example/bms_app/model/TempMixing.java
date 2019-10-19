package com.example.bms_app.model;

public class TempMixing {

    private int tempId;
    private int sfId;
    private int rmId;
    private float qty;
    private int prodHeaderId;

    public TempMixing(int tempId, int sfId, int rmId, float qty, int prodHeaderId) {
        this.tempId = tempId;
        this.sfId = sfId;
        this.rmId = rmId;
        this.qty = qty;
        this.prodHeaderId = prodHeaderId;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getSfId() {
        return sfId;
    }

    public void setSfId(int sfId) {
        this.sfId = sfId;
    }

    public int getRmId() {
        return rmId;
    }

    public void setRmId(int rmId) {
        this.rmId = rmId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public int getProdHeaderId() {
        return prodHeaderId;
    }

    public void setProdHeaderId(int prodHeaderId) {
        this.prodHeaderId = prodHeaderId;
    }

    @Override
    public String toString() {
        return "TempMixing{" +
                "tempId=" + tempId +
                ", sfId=" + sfId +
                ", rmId=" + rmId +
                ", qty=" + qty +
                ", prodHeaderId=" + prodHeaderId +
                '}';
    }
}
