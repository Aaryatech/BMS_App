package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.BmsCurrentStock;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> {
    private List<BmsCurrentStock> stockList;
    private Context context;

    public StockAdapter(List<BmsCurrentStock> stockList, Context context) {
        this.stockList = stockList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_stock_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.MyViewHolder myViewHolder, int i) {
        BmsCurrentStock model=stockList.get(i);
        String openStock = String.format("%.2f", model.getBmsOpeningStock());
        String prodQty = String.format("%.2f", model.getMixingIssueQty());
        String issueQty = String.format("%.2f", model.getProdIssueQty());
        String closeQty = String.format("%.2f", model.getBmsClosingStock());

        myViewHolder.tvMaterName.setText(""+model.getRmName());
        myViewHolder.tvOpenStock.setText(""+openStock);
        myViewHolder.tvProdQty.setText(""+prodQty);
        myViewHolder.tvIssueQty.setText(""+issueQty);
        myViewHolder.tvCloseQty.setText(""+closeQty);
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMaterName,tvOpenStock,tvProdQty,tvIssueQty,tvCloseQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaterName=(itemView).findViewById(R.id.tvMaterName);
            tvOpenStock=(itemView).findViewById(R.id.tvOpenStock);
            tvProdQty=(itemView).findViewById(R.id.tvProdQty);
            tvIssueQty=(itemView).findViewById(R.id.tvIssueQty);
            tvCloseQty=(itemView).findViewById(R.id.tvCloseQty);
        }
    }
}
