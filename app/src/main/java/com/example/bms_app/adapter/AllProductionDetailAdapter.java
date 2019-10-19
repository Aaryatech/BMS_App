package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.BillOfMaterialDetailed;

import java.util.List;

public class AllProductionDetailAdapter extends RecyclerView.Adapter<AllProductionDetailAdapter.MyViewHolder> {

    private List<BillOfMaterialDetailed> detailList;
    private Context context;

    public AllProductionDetailAdapter(List<BillOfMaterialDetailed> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllProductionDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_all_production_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductionDetailAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfMaterialDetailed model=detailList.get(i);
        myViewHolder.tvName.setText(""+model.getRmName());
        myViewHolder.tvAutoReqQty.setText(""+model.getAutoRmReqQty());
        myViewHolder.tvReqQty.setText(""+model.getRmReqQty());
        myViewHolder.tvIssueQty.setText(""+model.getRmIssueQty());
        myViewHolder.tvSingleCut.setText(""+model.getExVarchar1());
        myViewHolder.tvDoubleCut.setText(""+model.getExVarchar2());
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvAutoReqQty,tvReqQty,tvIssueQty,tvSingleCut,tvDoubleCut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvAutoReqQty=itemView.findViewById(R.id.tvAutoReqQty);
            tvReqQty=itemView.findViewById(R.id.tvReqQty);
            tvIssueQty=itemView.findViewById(R.id.tvIssueQty);
            tvSingleCut=itemView.findViewById(R.id.tvSingleCut);
            tvDoubleCut=itemView.findViewById(R.id.tvDoubleCut);

        }
    }
}
