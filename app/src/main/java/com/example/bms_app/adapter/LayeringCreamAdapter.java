package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.utils.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LayeringCreamAdapter extends RecyclerView.Adapter<LayeringCreamAdapter.MyViewHolder> {

    private List<SfPlanDetailForMixing> layeringCreamList;
    private List<SfItemDetail> sfItemDetailList = new ArrayList<>();
    private Context context;

    public LayeringCreamAdapter(List<SfPlanDetailForMixing> layeringCreamList, Context context) {
        this.layeringCreamList = layeringCreamList;
        this.context = context;
    }

    @NonNull
    @Override
    public LayeringCreamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_layering_cream_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LayeringCreamAdapter.MyViewHolder myViewHolder, int i) {
        final SfPlanDetailForMixing model=layeringCreamList.get(i);
        myViewHolder.tvItemName.setText(model.getRmName());

        myViewHolder.tvQty.setText(""+model.getTotal()/1000);
        myViewHolder.edEditQty.setText(""+0);
        myViewHolder.tvUOM.setText(""+model.getUom());

        Log.e("Decimal","--------------------------------------------------"+model.getTotal()/1000);
        Log.e("Model","--------------------------------------------------"+model);

        if(model.getRmType()==1)
        {
            myViewHolder.tvType.setText("rm");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvType.setText("sf");
        }

        myViewHolder.btnBOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strProd=myViewHolder.edEditQty.getText().toString();
                
                if(strProd.equalsIgnoreCase("0"))
                {
                    Toast.makeText(context, "Qty should be greater than 0", Toast.LENGTH_SHORT).show();
                }else {
                    getSfItemDetailsForCreamPrep(model.getRmId(), model);
                }

            }
        });

        myViewHolder.tvItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeptDialog1(context,model).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return layeringCreamList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty,tvUOM;
        public EditText edEditQty;
        public Button btnBOM;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
            btnBOM=itemView.findViewById(R.id.btnBOM);
        }
    }

    private void getSfItemDetailsForCreamPrep(Integer rmId, final SfPlanDetailForMixing model) {

        Log.e("PARAMETER","                 RM ID     "+rmId);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<SfItemHeader> listCall = Constants.myInterface.getSfItemDetailsForCreamPrep(rmId);
            listCall.enqueue(new Callback<SfItemHeader>() {
                @Override
                public void onResponse(Call<SfItemHeader> call, Response<SfItemHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION : ", " ----------------------SF ITEM DETAIL----------------------- " + response.body());
                            sfItemDetailList.clear();
                            sfItemDetailList=response.body().getSfItemDetail();

                            Log.e("LIST : ", " ----------------------LIST----------------------- " + sfItemDetailList);

                            List<SfItemDetail> sfItemTempList = new ArrayList<>();
                            for(int j=0;j<sfItemDetailList.size();j++)
                            {
                                Log.e("LIST : ", " ----------------------SIZE----------------------- " + sfItemDetailList.size());

                                int flag=0;
                                for(int k=0;k<sfItemTempList.size();k++)
                                {
                                    if(sfItemDetailList.get(j).getRmId()==sfItemTempList.get(k).getRmId() && sfItemDetailList.get(j).getRmType()==sfItemTempList.get(k).getRmType())
                                    {
                                        Log.e("LIST : ", " ----------------------EQUAL----------------------- ");
                                        sfItemTempList.get(k).setRmQty(sfItemTempList.get(k).getRmQty()+sfItemDetailList.get(j).getRmQty());
                                        Log.e("LIST : ", " ----------------------TOTAL----------------------- "+sfItemTempList.get(k).getRmQty());
                                        flag=1;
                                        break;

                                    }
                                }
                                if(flag==0)
                                {
                                    SfItemDetail sfItemDetail=sfItemDetailList.get(j);
                                    sfItemTempList.add(sfItemDetail);
                                }
                            }

                            new DeptDialog(context,model,sfItemTempList).show();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SfItemHeader> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private class DeptDialog extends Dialog {
        public Button btnSubmit;
        public RecyclerView recyclerView;
        SfPlanDetailForMixing model;
        List<SfItemDetail> sfItemTempList;
        LayringCreamDetailAdapter mAdapter;


        public DeptDialog(Context context, SfPlanDetailForMixing model, List<SfItemDetail> sfItemTempList) {
            super(context);
            this.model=model;
            this.sfItemTempList=sfItemTempList;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_layaring);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();

                }
            });

            Log.e("Dialog","--------------------List---------------------------------"+sfItemTempList);
            mAdapter = new LayringCreamDetailAdapter(sfItemTempList,context,model);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    private class DeptDialog1 extends Dialog {
        public Button btnAdd;
        public CheckBox checkbox1;
        public TextView tvProdId,tvQty,tvItem;
        public RecyclerView recyclerView;
        SfPlanDetailForMixing model;
        LayaringDetailAdapter mAdapter;


        public DeptDialog1(Context context, SfPlanDetailForMixing model) {
            super(context);
            this.model=model;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_layaring_detail);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            tvProdId = (TextView) findViewById(R.id.tvProdId);
            tvQty = (TextView) findViewById(R.id.tvQty);
            tvItem = (TextView) findViewById(R.id.tvItem);

            btnAdd = (Button) findViewById(R.id.btnAdd);
            checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


            tvItem.setText("Item : ");
            tvQty.setText("Prep Qty : ");
            tvProdId.setText("Prod Id : ");

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();

                }
            });

//            Log.e("Dialog","--------------------List---------------------------------"+sfItemTempList);
//            mAdapter = new LayaringDetailAdapter(sfItemTempList,context,model);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(mAdapter);
        }
    }
}
