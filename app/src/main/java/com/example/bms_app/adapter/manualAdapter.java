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

public class manualAdapter extends RecyclerView.Adapter<manualAdapter.MyViewHolder> {
    private List<SfItemDetail> issuesList;
    private Context context;


    public manualAdapter(List<SfItemDetail> issuesList, Context context) {
        this.issuesList = issuesList;
        this.context = context;
    }

    @NonNull
    @Override
    public manualAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_issues_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull manualAdapter.MyViewHolder myViewHolder, int i) {
        final SfItemDetail model=issuesList.get(i);

        myViewHolder.tvQty.setText(""+model.getRmQty());
        myViewHolder.edEditQty.setText(""+model.getRmQty());

        String strData=model.getRmName();
        String[] arrayString = strData.split("#");
        String name = arrayString[0];
        String unit = arrayString[1];

        myViewHolder.tvItemName.setText(name);
        myViewHolder.tvUOM.setText(""+unit);

        if(model.getRmType()==1)
        {
            myViewHolder.tvType.setText("RM");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvType.setText("SF");
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
                    model.setRmQty((int) editQty);
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
        return issuesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty,tvUOM;
        public EditText edEditQty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
        }
    }
}
