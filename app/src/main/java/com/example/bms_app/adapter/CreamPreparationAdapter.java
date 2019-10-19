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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.SfPlanDetailForMixing;

import java.util.List;

public class CreamPreparationAdapter extends RecyclerView.Adapter<CreamPreparationAdapter.MyViewHolder> {

    private List<SfPlanDetailForMixing> cpList;
    private Context context;

    public CreamPreparationAdapter(List<SfPlanDetailForMixing> cpList, Context context) {
        this.cpList = cpList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreamPreparationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_cream_preparation_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CreamPreparationAdapter.MyViewHolder myViewHolder, int i) {
        final SfPlanDetailForMixing model=cpList.get(i);
        final int pos = i;
        myViewHolder.tvItemName.setText(model.getRmName());

        myViewHolder.tvQty.setText(""+model.getTotal()/1000);
        myViewHolder.edEditQty.setText(""+model.getTotal()/1000);
        myViewHolder.tvUOM.setText(""+model.getUom());

        Log.e("Decimal","--------------------------------------------------"+model.getTotal()/1000);
        Log.e("Model","--------------------------------------------------"+model.getDoubleCut());

        if(model.getRmType()==1)
        {
            myViewHolder.tvType.setText("rm");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvType.setText("sf");
        }

        if(model.getDoubleCut()==1.0)
        {
            Log.e("False","---------------------------------");
            myViewHolder.checkBox1.setEnabled(false);
        }else if(model.getDoubleCut()==0.0)
        {
            Log.e("True","---------------------------------");
            myViewHolder.checkBox1.setEnabled(true);
        }

        myViewHolder.checkBox1.setChecked(cpList.get(i).getChecked());

        myViewHolder.checkBox1.setTag(cpList.get(i));

        myViewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SfPlanDetailForMixing sfPlanDetailForMixing = (SfPlanDetailForMixing) cb.getTag();

                sfPlanDetailForMixing.setChecked(cb.isChecked());
                cpList.get(pos).setChecked(cb.isChecked());

            }
        });

//        if(model.getChecked())
//        {
//            myViewHolder.checkBox1.setChecked(true);
//        }else{
//            myViewHolder.checkBox1.setChecked(false);
//        }
//
//
//        myViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                    model.setChecked(true);
//
//                } else {
//
//                    model.setChecked(false);
//
//                }
//
//            }
//        });


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
                    model.setEditTotal(editQty);
                    Log.e("DETAIL MODEL","------------------------------------------"+model);
                    Log.e("TEXT","------------------------------------------"+editQty);

                } catch (Exception e) {
                    float qty = 0;
                    model.setEditTotal(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty,tvUOM;
        public EditText edEditQty;
        public CheckBox checkBox1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
            checkBox1=itemView.findViewById(R.id.checkbox);
        }
    }
}
