package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.BillOfMaterialDetailed;

import java.util.List;

public class AllBomRequestAdapter extends RecyclerView.Adapter<AllBomRequestAdapter.MyViewHolder> {
    private List<BillOfMaterialDetailed> detailList;
    private Context context;

    public AllBomRequestAdapter(List<BillOfMaterialDetailed> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllBomRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_request_bom_detail_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBomRequestAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfMaterialDetailed model=detailList.get(i);
        myViewHolder.tvName.setText(""+model.getRmName());
        myViewHolder.tvAutoReqQty.setText(""+model.getAutoRmReqQty());
        myViewHolder.tvReqQty.setText(""+model.getRmReqQty());
        myViewHolder.edIssueQty.setText(""+model.getRmIssueQty());
        myViewHolder.tvSingleCut.setText(""+model.getExVarchar1());
        myViewHolder.tvDoubleCut.setText(""+model.getExVarchar2());

        myViewHolder.edIssueQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    float editQty = Float.parseFloat(charSequence.toString());
                    model.setRmReqQty(editQty);
                    Log.e("DETAIL MODEL","------------------------------------------"+model);

                } catch (Exception e) {
                    float qty = 0;
                    model.setRmReqQty(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvAutoReqQty,tvReqQty,tvSingleCut,tvDoubleCut;
        public EditText edIssueQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvAutoReqQty=itemView.findViewById(R.id.tvAutoReqQty);
            tvReqQty=itemView.findViewById(R.id.tvReqQty);
            edIssueQty=itemView.findViewById(R.id.edIssueQty);
            tvSingleCut=itemView.findViewById(R.id.tvSingleCut);
            tvDoubleCut=itemView.findViewById(R.id.tvDoubleCut);
        }
    }
}
