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

public class ProductionDetailAdapter extends RecyclerView.Adapter<ProductionDetailAdapter.MyViewHolder> {
    private List<BillOfMaterialDetailed> detailList;
    private Context context;

    public ProductionDetailAdapter(List<BillOfMaterialDetailed> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductionDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.production_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionDetailAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfMaterialDetailed model=detailList.get(i);
       // myViewHolder.tvItemName.setText(""+model.getRmName());

        myViewHolder.tvQty.setText(""+model.getAutoRmReqQty());
        //String editValue = String.format("%.3f", model.getAutoRmReqQty());
        myViewHolder.edEditQty.setText(""+model.getAutoRmReqQty());

        if(model.getRmType()==1)
        {
            myViewHolder.tvItemName.setText(""+model.getRmName()+"(RM)");
          //  myViewHolder.tvType.setText("RM");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvItemName.setText(""+model.getRmName()+"(SF)");
            //myViewHolder.tvType.setText("SF");
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
                        model.setRmReqQty(editQty);
                        Log.e("DETAIL MODEL","------------------------------------------"+model);

                    } catch (Exception e) {
                        float qty = 0;
                        model.setRmReqQty(0);
                    }
                }

        });


    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty;
        public EditText edEditQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
        }
    }
}
