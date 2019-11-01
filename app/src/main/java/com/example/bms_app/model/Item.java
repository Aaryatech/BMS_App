package com.example.bms_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemId")
    @Expose
    private String itemId;
    @SerializedName("itemName")
    @Expose
    private String itemName;
    @SerializedName("itemGrp1")
    @Expose
    private String itemGrp1;
    @SerializedName("itemGrp2")
    @Expose
    private String itemGrp2;
    @SerializedName("itemGrp3")
    @Expose
    private String itemGrp3;
    @SerializedName("itemRate1")
    @Expose
    private Integer itemRate1;
    @SerializedName("itemRate2")
    @Expose
    private Integer itemRate2;
    @SerializedName("itemMrp1")
    @Expose
    private Integer itemMrp1;
    @SerializedName("itemMrp2")
    @Expose
    private Integer itemMrp2;
    @SerializedName("itemMrp3")
    @Expose
    private Integer itemMrp3;
    @SerializedName("itemImage")
    @Expose
    private String itemImage;
    @SerializedName("itemTax1")
    @Expose
    private Integer itemTax1;
    @SerializedName("itemTax2")
    @Expose
    private Integer itemTax2;
    @SerializedName("itemTax3")
    @Expose
    private Integer itemTax3;
    @SerializedName("itemIsUsed")
    @Expose
    private Integer itemIsUsed;
    @SerializedName("itemSortId")
    @Expose
    private Integer itemSortId;
    @SerializedName("grnTwo")
    @Expose
    private Integer grnTwo;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("itemRate3")
    @Expose
    private Integer itemRate3;
    @SerializedName("minQty")
    @Expose
    private Integer minQty;
    @SerializedName("shelfLife")
    @Expose
    private Integer shelfLife;
    private boolean isChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemGrp1() {
        return itemGrp1;
    }

    public void setItemGrp1(String itemGrp1) {
        this.itemGrp1 = itemGrp1;
    }

    public String getItemGrp2() {
        return itemGrp2;
    }

    public void setItemGrp2(String itemGrp2) {
        this.itemGrp2 = itemGrp2;
    }

    public String getItemGrp3() {
        return itemGrp3;
    }

    public void setItemGrp3(String itemGrp3) {
        this.itemGrp3 = itemGrp3;
    }

    public Integer getItemRate1() {
        return itemRate1;
    }

    public void setItemRate1(Integer itemRate1) {
        this.itemRate1 = itemRate1;
    }

    public Integer getItemRate2() {
        return itemRate2;
    }

    public void setItemRate2(Integer itemRate2) {
        this.itemRate2 = itemRate2;
    }

    public Integer getItemMrp1() {
        return itemMrp1;
    }

    public void setItemMrp1(Integer itemMrp1) {
        this.itemMrp1 = itemMrp1;
    }

    public Integer getItemMrp2() {
        return itemMrp2;
    }

    public void setItemMrp2(Integer itemMrp2) {
        this.itemMrp2 = itemMrp2;
    }

    public Integer getItemMrp3() {
        return itemMrp3;
    }

    public void setItemMrp3(Integer itemMrp3) {
        this.itemMrp3 = itemMrp3;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Integer getItemTax1() {
        return itemTax1;
    }

    public void setItemTax1(Integer itemTax1) {
        this.itemTax1 = itemTax1;
    }

    public Integer getItemTax2() {
        return itemTax2;
    }

    public void setItemTax2(Integer itemTax2) {
        this.itemTax2 = itemTax2;
    }

    public Integer getItemTax3() {
        return itemTax3;
    }

    public void setItemTax3(Integer itemTax3) {
        this.itemTax3 = itemTax3;
    }

    public Integer getItemIsUsed() {
        return itemIsUsed;
    }

    public void setItemIsUsed(Integer itemIsUsed) {
        this.itemIsUsed = itemIsUsed;
    }

    public Integer getItemSortId() {
        return itemSortId;
    }

    public void setItemSortId(Integer itemSortId) {
        this.itemSortId = itemSortId;
    }

    public Integer getGrnTwo() {
        return grnTwo;
    }

    public void setGrnTwo(Integer grnTwo) {
        this.grnTwo = grnTwo;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getItemRate3() {
        return itemRate3;
    }

    public void setItemRate3(Integer itemRate3) {
        this.itemRate3 = itemRate3;
    }

    public Integer getMinQty() {
        return minQty;
    }

    public void setMinQty(Integer minQty) {
        this.minQty = minQty;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemGrp1='" + itemGrp1 + '\'' +
                ", itemGrp2='" + itemGrp2 + '\'' +
                ", itemGrp3='" + itemGrp3 + '\'' +
                ", itemRate1=" + itemRate1 +
                ", itemRate2=" + itemRate2 +
                ", itemMrp1=" + itemMrp1 +
                ", itemMrp2=" + itemMrp2 +
                ", itemMrp3=" + itemMrp3 +
                ", itemImage='" + itemImage + '\'' +
                ", itemTax1=" + itemTax1 +
                ", itemTax2=" + itemTax2 +
                ", itemTax3=" + itemTax3 +
                ", itemIsUsed=" + itemIsUsed +
                ", itemSortId=" + itemSortId +
                ", grnTwo=" + grnTwo +
                ", delStatus=" + delStatus +
                ", itemRate3=" + itemRate3 +
                ", minQty=" + minQty +
                ", shelfLife=" + shelfLife +
                ", isChecked=" + isChecked +
                '}';
    }
}
