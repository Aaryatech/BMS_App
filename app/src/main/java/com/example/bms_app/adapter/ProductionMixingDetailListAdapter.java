package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.MixingDetailed;

import java.util.List;

public class ProductionMixingDetailListAdapter extends RecyclerView.Adapter<ProductionMixingDetailListAdapter.MyViewHolder> {
    private List<MixingDetailed> mixingDetailList;
    private Context context;

    public ProductionMixingDetailListAdapter(List<MixingDetailed> mixingDetailList, Context context) {
        this.mixingDetailList = mixingDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductionMixingDetailListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.production_mixing_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionMixingDetailListAdapter.MyViewHolder myViewHolder, int i) {
        MixingDetailed model=mixingDetailList.get(i);
        myViewHolder.tvSfName.setText(""+model.getSfName());
        myViewHolder.tvOriginalQty.setText(""+model.getOriginalQty());
        myViewHolder.tvMultiplicationFactor.setText(""+model.getExVarchar1());
        myViewHolder.tvAutoOrderQty.setText(""+model.getAutoOrderQty());
        myViewHolder.tvReceivedQty.setText(""+model.getReceivedQty());
        myViewHolder.tvProductionQty.setText(""+model.getProductionQty());

    }

    @Override
    public int getItemCount() {
        return mixingDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSfName,tvOriginalQty,tvMultiplicationFactor,tvAutoOrderQty,tvReceivedQty,tvProductionQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSfName=itemView.findViewById(R.id.tvSfName);
            tvOriginalQty=itemView.findViewById(R.id.tvOriginalQty);
            tvMultiplicationFactor=itemView.findViewById(R.id.tvMultiplicationFactor);
            tvAutoOrderQty=itemView.findViewById(R.id.tvAutoOrderQty);
            tvReceivedQty=itemView.findViewById(R.id.tvReceivedQty);
            tvProductionQty=itemView.findViewById(R.id.tvProductionQty);
        }
    }
}
