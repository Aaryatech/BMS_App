package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.CurrentBmsSFStock;

import java.util.List;

public class StockSFAdapter extends RecyclerView.Adapter<StockSFAdapter.MyViewHolder>{
    private List<CurrentBmsSFStock> stockSFList;
    private Context context;

    public StockSFAdapter(List<CurrentBmsSFStock> stockSFList, Context context) {
        this.stockSFList = stockSFList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockSFAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_stock_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockSFAdapter.MyViewHolder myViewHolder, int i) {
        CurrentBmsSFStock model=stockSFList.get(i);
        String openStock = String.format("%.2f", model.getBmsOpeningStock());
        String prodQty = String.format("%.2f", model.getMixingIssueQty());
        String issueQty = String.format("%.2f", model.getProdIssueQty());
        String closeQty = String.format("%.2f", model.getBmsClosingStock());

        myViewHolder.tvMaterName.setText(""+model.getSfName());
        myViewHolder.tvOpenStock.setText(""+openStock);
        myViewHolder.tvProdQty.setText(""+prodQty);
        myViewHolder.tvIssueQty.setText(""+issueQty);
        myViewHolder.tvCloseQty.setText(""+closeQty);
    }

    @Override
    public int getItemCount() {
        return stockSFList.size();
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
