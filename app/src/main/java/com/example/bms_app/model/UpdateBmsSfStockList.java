package com.example.bms_app.model;

import java.util.List;

public class UpdateBmsSfStockList {

    List<UpdateBmsSfStock> bmsSfStock;

    public List<UpdateBmsSfStock> getBmsSfStock() {
        return bmsSfStock;
    }

    public void setBmsSfStock(List<UpdateBmsSfStock> bmsSfStock) {
        this.bmsSfStock = bmsSfStock;
    }

    @Override
    public String toString() {
        return "UpdateBmsSfStockList{" +
                "bmsSfStock=" + bmsSfStock +
                '}';
    }
}