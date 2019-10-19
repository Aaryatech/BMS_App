package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.SfPlanDetailForMixing;

import java.util.List;

public class LayaringDetailAdapter  extends RecyclerView.Adapter<LayaringDetailAdapter.MyViewHolder> {

    private List<SfPlanDetailForMixing> layeringCreamList;
    private Context context;

    @NonNull
    @Override
    public LayaringDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_layaring_detail_adpter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LayaringDetailAdapter.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvQty,tvUOM;
        public CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvQty=itemView.findViewById(R.id.tvQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
