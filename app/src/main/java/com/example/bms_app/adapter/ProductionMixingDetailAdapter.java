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
import com.example.bms_app.model.ProdMixingReqP1;

import java.text.DecimalFormat;
import java.util.List;

public class ProductionMixingDetailAdapter extends RecyclerView.Adapter<ProductionMixingDetailAdapter.MyViewHolder> {

    private List<ProdMixingReqP1> mixingDetailList;
    private Context context;
    private static DecimalFormat df = new DecimalFormat("0.0");

    public ProductionMixingDetailAdapter(List<ProdMixingReqP1> mixingDetailList, Context context) {
        this.mixingDetailList = mixingDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductionMixingDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.production_mixing_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductionMixingDetailAdapter.MyViewHolder myViewHolder, int i) {
        final ProdMixingReqP1 model=mixingDetailList.get(i);
        myViewHolder.tvRmName.setText(""+model.getRmName());
        myViewHolder.tvQty.setText(""+model.getTotal());
        myViewHolder.tvMultiplicationFactor.setText(""+model.getMulFactor());
        myViewHolder.tvReqQty.setText(""+model.getTotal()*model.getMulFactor());
        myViewHolder.tvUnit.setText(""+model.getUom());
       // myViewHolder.edEditReqQty.setText(""+model.getTotal()*model.getMulFactor());

        //double roundOff = Math.round(model.getTotal()*model.getMulFactor() * 10d) / 10d;
        Double roundOff =Math.ceil(model.getTotal()*model.getMulFactor());

        Log.e("ROUND OFF","-----------------------------------"+roundOff);
        myViewHolder.edEditReqQty.setText(""+roundOff);

        try {
            model.setPrevtotal(model.getTotal());
            float editQty = model.getTotal()*model.getMulFactor();
          //  float editQty = model.getTotal();
            model.setTotal(editQty);
            Log.e(" PREV TOTAL","------------------------------------------"+model.getPrevtotal());
            Log.e(" TOTAL","------------------------------------------"+model.getTotal());
            Log.e(" BIN","------------------------------------------"+model);

        } catch (Exception e) {
            float qty = 0;
            model.setPrevtotal(0);
            model.setTotal(0);
        }
        myViewHolder.edEditReqQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("Text","------------------------------------------"+editable);
                try {
                    Log.e("Total","---------------------------"+model.getTotal());
                    Log.e("Prev Total","---------------------------"+model.getPrevtotal());
                 //   model.setPrevtotal(model.getTotal());
                    float editQty = Float.parseFloat(editable.toString());
                    model.setTotal(editQty);
                    Log.e("DETAIL PREV TOTAL","------------------------------------------"+model.getPrevtotal());
                    Log.e("DETAIL TOTAL","------------------------------------------"+model.getTotal());
                    Log.e("DETAIL BIN","------------------------------------------"+model);

                } catch (Exception e) {
                    float qty = 0;
                   // model.setPrevtotal(0);
                    model.setTotal(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mixingDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRmName,tvQty,tvMultiplicationFactor,tvReqQty,tvUnit;
        public EditText edEditReqQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRmName=itemView.findViewById(R.id.tvRmName);
            tvQty=itemView.findViewById(R.id.tvQty);
            tvMultiplicationFactor=itemView.findViewById(R.id.tvMultiplicationFactor);
            tvReqQty=itemView.findViewById(R.id.tvReqQty);
            tvUnit=itemView.findViewById(R.id.tvUnit);
            edEditReqQty=itemView.findViewById(R.id.edEditReqQty);
        }
    }
}
