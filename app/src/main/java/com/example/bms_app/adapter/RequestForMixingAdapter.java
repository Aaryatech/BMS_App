package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.activity.MainActivity;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.fragment.RequestForMixingFragment;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.utils.CommonDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForMixingAdapter extends RecyclerView.Adapter<RequestForMixingAdapter.MyViewHolder> {
    private List<ProdPlanHeader> reqMixingList;
    private List<SfPlanDetailForMixing> detailList = new ArrayList<>();
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    ArrayList<BillOfMaterialDetailed> billDetailList = new ArrayList<>();
    private Context context;
    int fromId=0,toId=0;
    String fromName,toName;
    Login login;
     CommonDialog commonDialog1;

    public RequestForMixingAdapter(List<ProdPlanHeader> reqMixingList, Context context, Login login) {
        this.reqMixingList = reqMixingList;
        this.context = context;
        this.login = login;
    }

    @NonNull
    @Override
    public RequestForMixingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_production_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestForMixingAdapter.MyViewHolder myViewHolder, final int i) {
        final ProdPlanHeader model=reqMixingList.get(i);
        myViewHolder.tvProductId.setText(""+model.getProductionHeaderId());
        myViewHolder.tvDate.setText(model.getProductionDate());

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

//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(model.getProductionStatus().equalsIgnoreCase("5"))
//                {
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                }else{
//                    getProductionSetting("MIX",model.getProductionHeaderId(),model);
//                }
//
//
//            }
//        });

        myViewHolder.tvMixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProductionSetting("MIX",model.getProductionHeaderId(),model);
               // getSettingValue("PROD");
               // new DeptDialog(context,model,"MIX").show();
            }
        });

        myViewHolder.tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProductionSetting("STORE",model.getProductionHeaderId(),model);
              //  new DeptDialog(context,model,"STORE").show();
            }
        });

        if(model.getIsMixing()==0)
        {
            Log.e("MIXING","----------------------------------------");
            if(model.getProductionStatus().equalsIgnoreCase("5"))
            {
                myViewHolder.tvMixing.setVisibility(View.GONE);
            }else{
                myViewHolder.tvMixing.setVisibility(View.VISIBLE);
            }

        }else if(model.getIsMixing()==1)
        {
            myViewHolder.tvMixing.setVisibility(View.GONE);
        }
//        if(model.getIsStoreBom()==0)
//        {
//            myViewHolder.tvStore.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return reqMixingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvIsPlane;
        public TextView tvMixing,tvStore;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvIsPlane=itemView.findViewById(R.id.tvIsPlane);

            cardView=itemView.findViewById(R.id.cardView);

            tvMixing=itemView.findViewById(R.id.ivMixing);
            tvStore=itemView.findViewById(R.id.ivStore);
        }
    }

    private class DeptDialog extends Dialog {
        public Button btnSubmit,btnCancel;
        public RecyclerView recyclerView;
        private ProductionDetailAdapter mAdapter;
        //ProdPlanHeader prodDetail;
       // int productionHeaderId;
        ProdPlanHeader prodPlanHeader;
        String dept;

        public DeptDialog(Context context,ProdPlanHeader prodPlanHeader,String dept) {
            super(context);
            this.prodPlanHeader=prodPlanHeader;
            this.dept=dept;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_production_detail);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//            getProductionSetting(dept,prodPlanHeader.getProductionHeaderId());
            getSettingValue("PROD");

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                   // sdf.format(System.currentTimeMillis())
//                    String dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    String currentDate = formatter.format(todayDate);
                    Log.e("Mytag","---------------------------todayString-------------------------------"+currentDate);

                    BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(),sdf1.format(System.currentTimeMillis()),1,fromId,fromName,toId,toName,login.getUser().getId(),sdf1.format(System.currentTimeMillis()),login.getUser().getId(),sdf1.format(System.currentTimeMillis()),0,0,0,0,0,"","",prodPlanHeader.getIsPlanned(),0,0,sdf.format(System.currentTimeMillis()),0,sdf.format(System.currentTimeMillis()),billDetailList);
                    saveDetail(billOfMaterialHeader,prodPlanHeader);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (detailList != null) {
                billDetailList.clear();
                for (int i = 0; i < detailList.size(); i++) {
                    BillOfMaterialDetailed billOfMaterialDetailed=new BillOfMaterialDetailed(0,detailList.get(i).getItemDetailId(),detailList.get(i).getRmType(),detailList.get(i).getRmId(),detailList.get(i).getRmName(),detailList.get(i).getUom(),detailList.get(i).getRmQty(),detailList.get(i).getRmQty(),0,0,String.valueOf(detailList.get(i).getSingleCut()),String.valueOf(detailList.get(i).getDoubleCut()),"",0,0,0,detailList.get(i).getTotal(),0,0);
                    billDetailList.add(billOfMaterialDetailed);
                }
                Log.e("Mytag","---------------------------BILL DETAIL-------------------------------"+billDetailList);
                mAdapter = new ProductionDetailAdapter(billDetailList,context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        }


    }

    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION MATERIAL HEADER--------------------------"+billOfMaterialHeader);

        if (Constants.isOnline(context)) {
            commonDialog1 = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog1.show();

            Call<Info> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " ------------------------------SAVE PRODUCTION HEADER------------------------ " + response.body());
                           // Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            getupdateisMixingandBom(prodPlanHeader.getProductionHeaderId(),1,toId);

                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("" + context.getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("" + context.getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("" + context.getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getupdateisMixingandBom(Integer productionHeaderId, int flag, Integer dept) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId+"      FLAG      "+flag+"          DEPT            "+dept);
        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<Integer> listCall = Constants.myInterface.updateisMixingandBom(productionHeaderId,flag,dept);
            listCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE : ", " ------------------------------UPDATE MIXING------------------------ " + response.body());
                            MainActivity activity=(MainActivity)context;
                            FragmentTransaction ft =activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new RequestForMixingFragment(), "MainFragment");
                            ft.commit();
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("" + context.getResources().getString(R.string.app_name));
                            builder.setMessage("Unable to process! please try again.");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("" + context.getResources().getString(R.string.app_name));
                        builder.setMessage("Unable to process! please try again.");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("" + context.getResources().getString(R.string.app_name));
                    builder.setMessage("Unable to process! please try again.");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSettingValue(String prod) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+prod);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(prod);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE PROD: ", " - " + response.body());
                            Configure configure=response.body();
                          //  frItemStockConfiguresList.clear();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            if(frItemStockConfiguresList!=null)
                            {
                                if(frItemStockConfiguresList.size()>0)
                                {
                                    // if(settingKey.equalsIgnoreCase("PROD"))
                                    // {
                                    fromId=frItemStockConfiguresList.get(0).getSettingValue();
                                    fromName=frItemStockConfiguresList.get(0).getSettingKey();
                                    //}
                                }
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
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

    private void getProductionSetting(final String settingKey, final int productionHeaderId, final ProdPlanHeader model) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+settingKey  +"               HEADER           "+productionHeaderId);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(settingKey);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE MIX: ", " - " + response.body());
                           // frItemStockConfiguresList.clear();
                            Configure configure=response.body();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getProductionDetail(productionHeaderId, configure.getFrItemStockConfigure().get(i).getSettingValue(),settingKey,model);
                            }
                            if(frItemStockConfiguresList!=null)
                            {
                                if(frItemStockConfiguresList.size()>0)
                                {
                                   // if(settingKey.equalsIgnoreCase("PROD"))
                                   // {
                                         toId=frItemStockConfiguresList.get(0).getSettingValue();
                                         toName=frItemStockConfiguresList.get(0).getSettingKey();
                                    //}
                                }
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
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

    private void getProductionDetail(Integer productionHeaderId, Integer deptId, final String dept, final ProdPlanHeader model) {

        Log.e("PARAMETER","                 HEADER ID     "+productionHeaderId  +"               SETTING VALUES           "+deptId);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<DeptDetail> listCall = Constants.myInterface.getSfPlanDetailForBom(productionHeaderId,deptId);
            listCall.enqueue(new Callback<DeptDetail>() {
                @Override
                public void onResponse(Call<DeptDetail> call, Response<DeptDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION DETAIL: ", " - " + response.body());
                            detailList.clear();
                            detailList=response.body().getSfPlanDetailForMixing();
                            new DeptDialog(context,model,dept).show();
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
                public void onFailure(Call<DeptDetail> call, Throwable t) {
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
}
