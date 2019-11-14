package com.example.bms_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bms_app.R;
import com.example.bms_app.model.ProdPlanHeader;

import java.util.List;

public class RequestForBMSAdapter extends RecyclerView.Adapter<RequestForBMSAdapter.MyViewHolder>  {

    private List<ProdPlanHeader> reqMixingList;
    private Context context;
    public int mSelectedItem = -1;
    private boolean onBind;


    public RequestForBMSAdapter(List<ProdPlanHeader> reqMixingList, Context context) {
        this.reqMixingList = reqMixingList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestForBMSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_bms_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestForBMSAdapter.MyViewHolder myViewHolder, final int i) {
        final ProdPlanHeader model=reqMixingList.get(i);
        final int pos = i;
        myViewHolder.tvProductId.setText(""+model.getProductionHeaderId());
        myViewHolder.tvDate.setText(model.getProductionDate());
        myViewHolder.tvCatName.setText(model.getCatName());
      //  myViewHolder.rbSelect.setChecked(i == mSelectedItem);


        if(model.getIsPlanned()==1) {
            myViewHolder.tvIsPlane.setText("Yes");
        }else if(model.getIsPlanned()==0)
        {
            myViewHolder.tvIsPlane.setText("No");
        }

        if(model.getProductionStatus().equalsIgnoreCase("1")) {
            myViewHolder.tvStatus.setText("Add From Plan");
        }else if(model.getProductionStatus().equalsIgnoreCase("2"))
        {
            myViewHolder.tvStatus.setText("Add From Production");
        }else if(model.getProductionStatus().equalsIgnoreCase("3"))
        {
            myViewHolder.tvStatus.setText("Start Production");
        }else if(model.getProductionStatus().equalsIgnoreCase("4"))
        {
            myViewHolder.tvStatus.setText("Production Completed");
        }else if(model.getProductionStatus().equalsIgnoreCase("5"))
        {
            myViewHolder.tvStatus.setText("Closed");
        }
        onBind = true;
        if(model.getChecked())
        {
            myViewHolder.rbSelect.setChecked(true);
        }else{
            myViewHolder.rbSelect.setChecked(false);
        }
        onBind = false;
        myViewHolder.rbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!onBind) {

                    if (isChecked) {
                        //model.setChecked(true);
                        model.setChecked(true);

                        for (int i = 0; i < reqMixingList.size(); i++) {
                            Log.e("For", "-------------------------------------------");
                            if (reqMixingList.get(i).getProductionHeaderId() != model.getProductionHeaderId()) {
                                Log.e("Equal", "-------------------------------------------");
                                reqMixingList.get(i).setChecked(false);
                            }

                        }
                        notifyDataSetChanged();

                        Log.e("RADIO", "------------------------------model-----------------------------" + model);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reqMixingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvIsPlane,tvCatName;
        public RadioButton rbSelect;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvIsPlane=itemView.findViewById(R.id.tvIsPlane);
            rbSelect=itemView.findViewById(R.id.rbSelect);
            tvCatName=itemView.findViewById(R.id.tvCatName);

//            View.OnClickListener clickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("OnClick","------------------Selected Item---------------------"+mSelectedItem);
//                    mSelectedItem = getAdapterPosition();
//                    Log.e("OnClick1","------------------Selected Item---------------------"+mSelectedItem);
//                    new BMSListFragment().onClickData(mSelectedItem,context,"radioOnclick");
//                    notifyDataSetChanged();
//                }
//            };
//            itemView.setOnClickListener(clickListener);
//            rbSelect.setOnClickListener(clickListener);
        }
    }
}
