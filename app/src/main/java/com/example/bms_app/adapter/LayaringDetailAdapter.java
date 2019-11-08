package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class LayaringDetailAdapter  extends RecyclerView.Adapter<LayaringDetailAdapter.MyViewHolder> {

    private List<SfPlanDetailForMixing> layeringCreamList;
    private Context context;
    SfPlanDetailForMixing model;
    EditText mEditText;

    public LayaringDetailAdapter(List<SfPlanDetailForMixing> layeringCreamList, Context context, SfPlanDetailForMixing model,EditText mEditText) {
        this.layeringCreamList = layeringCreamList;
        this.context = context;
        this.model = model;
        this.mEditText = mEditText;
    }

    @NonNull
    @Override
    public LayaringDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_layaring_detail_adpter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LayaringDetailAdapter.MyViewHolder myViewHolder, int i) {
        final SfPlanDetailForMixing model=layeringCreamList.get(i);
        final int pos = i;
        myViewHolder.tvItemName.setText(model.getRmName());
        myViewHolder.tvQty.setText(""+model.getTotal());
        myViewHolder.tvUOM.setText(model.getUom());

        if(model.getDoubleCut()==1.0)
        {
            Log.e("False","---------------------------------"+model.getDoubleCut());
            //myViewHolder.checkBox1.setEnabled(false);
            myViewHolder.checkBox1.setVisibility(View.INVISIBLE);
            model.setChecked(false);
        }else if(model.getDoubleCut()==0.0)
        {
            Log.e("True","---------------------------------"+model.getDoubleCut());
           // myViewHolder.checkBox1.setEnabled(true);
            myViewHolder.checkBox1.setVisibility(View.VISIBLE);
        }

        myViewHolder.checkBox1.setChecked(layeringCreamList.get(i).getChecked());

        myViewHolder.checkBox1.setTag(layeringCreamList.get(i));

        myViewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SfPlanDetailForMixing sfPlanDetailForMixing = (SfPlanDetailForMixing) cb.getTag();

                sfPlanDetailForMixing.setChecked(cb.isChecked());
                layeringCreamList.get(pos).setChecked(cb.isChecked());
                notifyDataSetChanged();

//                if(model.getChecked()){
//                    totalamount+=model.getTotal();
//                }else{
//                    totalamount-=model.getTotal();
//                }
//                model.setProdQty(totalamount);
//                String no = String.format("%.3f", totalamount);
//                mEditText.setText(""+no);
//                Toast.makeText(context, ""+model.getProdQty(), Toast.LENGTH_LONG).show();

                float totalamount=0;
                //int flag =0;
                    Log.e("MYTAG", "--------------------------------------CHECK------------------------------------" +layeringCreamList.get(pos).getChecked());
                for(int k=0;k<layeringCreamList.size();k++) {

                    if(layeringCreamList.get(k).getChecked()) {
                       // flag =1;
                        totalamount+=layeringCreamList.get(k).getTotal();
                        //mEditText.setText("" + totalamount);
                    }
//                    else{
//                        totalamount-= layeringCreamList.get(k).getTotal();
//                        mEditText.setText("" + totalamount);
//                    }

                }
                String no = String.format("%.3f", totalamount);
                mEditText.setText("" + no);
//                if(flag==0)
//                {
//                    mEditText.setText("0");
//                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return layeringCreamList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvQty,tvUOM;
        public CheckBox checkBox1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvQty=itemView.findViewById(R.id.tvQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
            checkBox1=itemView.findViewById(R.id.checkbox);
        }
    }
}
