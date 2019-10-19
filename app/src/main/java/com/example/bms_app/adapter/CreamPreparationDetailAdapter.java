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
import com.example.bms_app.model.SfItemDetail;

import java.util.List;

public class CreamPreparationDetailAdapter extends RecyclerView.Adapter<CreamPreparationDetailAdapter.MyViewHolder> {
    private List<SfItemDetail> cpDetailList;
    private Context context;

    public CreamPreparationDetailAdapter(List<SfItemDetail> cpDetailList, Context context) {
        this.cpDetailList = cpDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreamPreparationDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_cream_preparation_detail_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CreamPreparationDetailAdapter.MyViewHolder myViewHolder, int i) {
        final SfItemDetail model=cpDetailList.get(i);
        myViewHolder.tvItemName.setText(model.getRmName());
        int srNo=i+1;
        myViewHolder.tvSrNo.setText(""+srNo);

        myViewHolder.tvQty.setText(""+model.getRmQty()/1000);
        myViewHolder.edEditQty.setText(""+model.getRmQty()/1000);
        myViewHolder.tvUOM.setText(""+model.getRmUnit());

        Log.e("Decimal","--------------------------------------------------"+model.getRmQty());
        Log.e("Model","--------------------------------------------------"+model);
        Log.e("Model","------------------------Sr No--------------------------"+srNo);

        if(model.getRmType()==1)
        {
            myViewHolder.tvType.setText("rm");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvType.setText("sf");
        }

        myViewHolder.edEditQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    float editQty = Float.parseFloat(editable.toString());
                    model.setRmQty(editQty);
                    Log.e("DETAIL MODEL","------------------------------------------"+model);

                } catch (Exception e) {
                    float qty = 0;
                    model.setRmQty(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cpDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty,tvUOM,tvSrNo;
        public EditText edEditQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSrNo=itemView.findViewById(R.id.tvSrNo);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);

        }
    }
}
