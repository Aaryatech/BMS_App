package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.BillOfAllMaterialHeader;

import java.util.List;

public class IssueFromBMSAdapter extends RecyclerView.Adapter<IssueFromBMSAdapter.MyViewHolder> {
    private List<BillOfAllMaterialHeader> reqAllList;
    private Context context;


    public IssueFromBMSAdapter(List<BillOfAllMaterialHeader> reqAllList, Context context) {
        this.reqAllList = reqAllList;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueFromBMSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_all_request, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueFromBMSAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfAllMaterialHeader model=reqAllList.get(i);

        Log.e("ADAPTER LIST","-------------------------------------------------------"+reqAllList);

        myViewHolder.tvProductId.setText(""+model.getProductionId());
        myViewHolder.tvDate.setText(""+model.getReqDate());
        myViewHolder.tvDept.setText(""+model.getToDeptName());

        if(model.getStatus()==0)
        {
            myViewHolder.tvStatus.setText("Pending");
        }else if(model.getStatus()==1)
        {
            myViewHolder.tvStatus.setText("Approved");
        }if(model.getStatus()==2)
        {
            myViewHolder.tvStatus.setText("Rejected");
        }if(model.getStatus()==3)
        {
            myViewHolder.tvStatus.setText("Approved Rejected");
        }if(model.getStatus()==4)
        {
            myViewHolder.tvStatus.setText("Request Close");
        }

    }

    @Override
    public int getItemCount() {
        return reqAllList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvDept;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDept=itemView.findViewById(R.id.tvDept);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
