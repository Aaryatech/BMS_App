package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillOfAllMaterialHeader {
    @SerializedName("reqId")
    @Expose
    private Integer reqId;
    @SerializedName("productionId")
    @Expose
    private Integer productionId;
    @SerializedName("productionDate")
    @Expose
    private String productionDate;
    @SerializedName("isProduction")
    @Expose
    private Integer isProduction;
    @SerializedName("fromDeptId")
    @Expose
    private Integer fromDeptId;
    @SerializedName("fromDeptName")
    @Expose
    private String fromDeptName;
    @SerializedName("toDeptId")
    @Expose
    private Integer toDeptId;
    @SerializedName("toDeptName")
    @Expose
    private String toDeptName;
    @SerializedName("senderUserid")
    @Expose
    private Integer senderUserid;
    @SerializedName("reqDate")
    @Expose
    private String reqDate;
    @SerializedName("approvedUserId")
    @Expose
    private Integer approvedUserId;
    @SerializedName("approvedDate")
    @Expose
    private String approvedDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("exBool1")
    @Expose
    private Integer exBool1;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exInt2")
    @Expose
    private Integer exInt2;
    @SerializedName("exVarchar1")
    @Expose
    private Object exVarchar1;
    @SerializedName("exVarchar2")
    @Expose
    private Object exVarchar2;
    @SerializedName("billOfMaterialDetailed")
    @Expose
    private Object billOfMaterialDetailed;
    @SerializedName("isPlan")
    @Expose
    private Integer isPlan;
    @SerializedName("isManual")
    @Expose
    private Integer isManual;
    @SerializedName("rejUserId")
    @Expose
    private Integer rejUserId;
    @SerializedName("rejDate")
    @Expose
    private String rejDate;
    @SerializedName("rejApproveUserId")
    @Expose
    private Integer rejApproveUserId;
    @SerializedName("rejApproveDate")
    @Expose
    private String rejApproveDate;

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public Integer getProductionId() {
        return productionId;
    }

    public void setProductionId(Integer productionId) {
        this.productionId = productionId;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public Integer getIsProduction() {
        return isProduction;
    }

    public void setIsProduction(Integer isProduction) {
        this.isProduction = isProduction;
    }

    public Integer getFromDeptId() {
        return fromDeptId;
    }

    public void setFromDeptId(Integer fromDeptId) {
        this.fromDeptId = fromDeptId;
    }

    public String getFromDeptName() {
        return fromDeptName;
    }

    public void setFromDeptName(String fromDeptName) {
        this.fromDeptName = fromDeptName;
    }

    public Integer getToDeptId() {
        return toDeptId;
    }

    public void setToDeptId(Integer toDeptId) {
        this.toDeptId = toDeptId;
    }

    public String getToDeptName() {
        return toDeptName;
    }

    public void setToDeptName(String toDeptName) {
        this.toDeptName = toDeptName;
    }

    public Integer getSenderUserid() {
        return senderUserid;
    }

    public void setSenderUserid(Integer senderUserid) {
        this.senderUserid = senderUserid;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public Integer getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(Integer approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getExBool1() {
        return exBool1;
    }

    public void setExBool1(Integer exBool1) {
        this.exBool1 = exBool1;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public Object getExVarchar1() {
        return exVarchar1;
    }

    public void setExVarchar1(Object exVarchar1) {
        this.exVarchar1 = exVarchar1;
    }

    public Object getExVarchar2() {
        return exVarchar2;
    }

    public void setExVarchar2(Object exVarchar2) {
        this.exVarchar2 = exVarchar2;
    }

    public Object getBillOfMaterialDetailed() {
        return billOfMaterialDetailed;
    }

    public void setBillOfMaterialDetailed(Object billOfMaterialDetailed) {
        this.billOfMaterialDetailed = billOfMaterialDetailed;
    }

    public Integer getIsPlan() {
        return isPlan;
    }

    public void setIsPlan(Integer isPlan) {
        this.isPlan = isPlan;
    }

    public Integer getIsManual() {
        return isManual;
    }

    public void setIsManual(Integer isManual) {
        this.isManual = isManual;
    }

    public Integer getRejUserId() {
        return rejUserId;
    }

    public void setRejUserId(Integer rejUserId) {
        this.rejUserId = rejUserId;
    }

    public String getRejDate() {
        return rejDate;
    }

    public void setRejDate(String rejDate) {
        this.rejDate = rejDate;
    }

    public Integer getRejApproveUserId() {
        return rejApproveUserId;
    }

    public void setRejApproveUserId(Integer rejApproveUserId) {
        this.rejApproveUserId = rejApproveUserId;
    }

    public String getRejApproveDate() {
        return rejApproveDate;
    }

    public void setRejApproveDate(String rejApproveDate) {
        this.rejApproveDate = rejApproveDate;
    }

    @Override
    public String toString() {
        return "BillOfAllMaterialHeader{" +
                "reqId=" + reqId +
                ", productionId=" + productionId +
                ", productionDate='" + productionDate + '\'' +
                ", isProduction=" + isProduction +
                ", fromDeptId=" + fromDeptId +
                ", fromDeptName='" + fromDeptName + '\'' +
                ", toDeptId=" + toDeptId +
                ", toDeptName='" + toDeptName + '\'' +
                ", senderUserid=" + senderUserid +
                ", reqDate='" + reqDate + '\'' +
                ", approvedUserId=" + approvedUserId +
                ", approvedDate='" + approvedDate + '\'' +
                ", status=" + status +
                ", exBool1=" + exBool1 +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVarchar1=" + exVarchar1 +
                ", exVarchar2=" + exVarchar2 +
                ", billOfMaterialDetailed=" + billOfMaterialDetailed +
                ", isPlan=" + isPlan +
                ", isManual=" + isManual +
                ", rejUserId=" + rejUserId +
                ", rejDate=" + rejDate +
                ", rejApproveUserId=" + rejApproveUserId +
                ", rejApproveDate=" + rejApproveDate +
                '}';
    }
}
