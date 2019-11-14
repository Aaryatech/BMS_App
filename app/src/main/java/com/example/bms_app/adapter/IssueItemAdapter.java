package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.BillOfMaterialDetailed;

import java.util.List;

public class IssueItemAdapter extends RecyclerView.Adapter<IssueItemAdapter.MyViewHolder> {
    private List<BillOfMaterialDetailed> detailList;
    private Context context;

    public IssueItemAdapter(List<BillOfMaterialDetailed> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_issue, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueItemAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfMaterialDetailed model=detailList.get(i);
        myViewHolder.tvName.setText(""+model.getRmName());
        myViewHolder.tvReqQty.setText(""+model.getRmReqQty());


    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvReqQty;
        public EditText edIssueQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvReqQty=itemView.findViewById(R.id.tvReqQty);

        }
    }
}
