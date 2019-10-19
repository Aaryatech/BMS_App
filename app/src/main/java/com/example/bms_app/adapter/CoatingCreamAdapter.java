package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.activity.MainActivity;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.fragment.BMSListFragment;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.GetTempMixItemDetailList;
import com.example.bms_app.model.MixingDetailedSave;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.model.TempMixItemDetail;
import com.example.bms_app.model.TempMixing;
import com.example.bms_app.utils.CommonDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoatingCreamAdapter extends RecyclerView.Adapter<CoatingCreamAdapter.MyViewHolder> {

    private List<SfPlanDetailForMixing> cotingCreamList;
    private List<SfItemDetail> sfItemDetailList = new ArrayList<>();
    private Context context;
    ProdPlanHeader prodPlanHeader;

    private List<FrItemStockConfigure> frItemStockConfiguresList;
    ArrayList<TempMixing> tempMixingDetailList = new ArrayList<>();
    private List<TempMixItemDetail> mixDetailList = new ArrayList<>();
    ArrayList<MixingDetailedSave> mixList = new ArrayList<>();
    ArrayList<BillOfMaterialDetailed> billDetailList = new ArrayList<>();
    int fromId=0,toId=0;
    String fromName,toName;
    CommonDialog commonDialog1;
    List<SfItemDetail> sfItemTempList = new ArrayList<>();

    public CoatingCreamAdapter(List<SfPlanDetailForMixing> cotingCreamList, Context context,ProdPlanHeader prodPlanHeader) {
        this.cotingCreamList = cotingCreamList;
        this.context = context;
        this.prodPlanHeader = prodPlanHeader;
    }

    @NonNull
    @Override
    public CoatingCreamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_coting_cream_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoatingCreamAdapter.MyViewHolder myViewHolder, int i) {
        final SfPlanDetailForMixing model=cotingCreamList.get(i);
        myViewHolder.tvItemName.setText(model.getRmName());

        myViewHolder.tvQty.setText(""+model.getTotal()/1000);
        myViewHolder.edEditQty.setText(""+model.getTotal()/1000);
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
                    model.setEditTotal((int) editQty);
                    Log.e("DETAIL MODEL","------------------------------------------"+model);

                } catch (Exception e) {
                    float qty = 0;
                    model.setEditTotal(0);
                }
            }
        });

        myViewHolder.btnBOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSfItemDetailsForCreamPrep(model.getRmId(),model,prodPlanHeader);

            }
        });
    }

    private void getSfItemDetailsForCreamPrep(Integer rmId, final SfPlanDetailForMixing model, final ProdPlanHeader prodPlanHeader) {

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

                          //  List<SfItemDetail> sfItemTempList = new ArrayList<>();
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

                            new DeptDialog(context,model,sfItemTempList,prodPlanHeader).show();


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

    @Override
    public int getItemCount() {
        return cotingCreamList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvType,tvQty,tvUOM,tvSrNo;
        public EditText edEditQty;
        public Button btnBOM;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tvItemName);
            tvType=itemView.findViewById(R.id.tvType);
            tvQty=itemView.findViewById(R.id.tvQty);
            edEditQty=itemView.findViewById(R.id.edEditQty);
            tvUOM=itemView.findViewById(R.id.tvUOM);
            tvSrNo=itemView.findViewById(R.id.tvSrNo);
            btnBOM=itemView.findViewById(R.id.btnBOM);
        }
    }

    private class DeptDialog extends Dialog {
        public Button btnSubmit;
        public RecyclerView recyclerView;
        SfPlanDetailForMixing model;
        List<SfItemDetail> sfItemTempList;
        CoatingCreamDetailAdapter mAdapter;
        ProdPlanHeader prodPlanHeader;



        public DeptDialog(Context context, SfPlanDetailForMixing model, List<SfItemDetail> sfItemTempList, ProdPlanHeader prodPlanHeader) {
            super(context);
            this.model=model;
            this.sfItemTempList=sfItemTempList;
            this.prodPlanHeader=prodPlanHeader;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_coting_detail);
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

            getSettingValue("PROD");
            getSettingKey("BMS");

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();

                        TempMixing tempMixing = new TempMixing(0, 0, model.getRmId(), model.getTotal(), prodPlanHeader.getProductionHeaderId());
                        tempMixingDetailList.add(tempMixing);

                    saveTempMixing(tempMixingDetailList, prodPlanHeader);

                }
            });

                Log.e("Dialog","--------------------List---------------------------------"+sfItemTempList);
                mAdapter = new CoatingCreamDetailAdapter(sfItemTempList,context,model);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
        }

    private void saveTempMixing(ArrayList<TempMixing> tempMixing, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING TEMP--------------------------"+tempMixing);

        if (Constants.isOnline(context)) {
            commonDialog1 = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog1.show();

            Call<TempMixing> listCall = Constants.myInterface.insertTempMixing(tempMixing);
            listCall.enqueue(new Callback<TempMixing>() {
                @Override
                public void onResponse(Call<TempMixing> call, Response<TempMixing> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE : ", " ------------------------------SAVE TEMP MIXING------------------------ " + response.body());
                            getTempMixItemDetail(prodPlanHeader.getProductionHeaderId(),prodPlanHeader);

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
                public void onFailure(Call<TempMixing> call, Throwable t) {
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

    private void getTempMixItemDetail(Integer productionHeaderId, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId);
        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<GetTempMixItemDetailList> listCall = Constants.myInterface.getTempMixItemDetail(productionHeaderId);
            listCall.enqueue(new Callback<GetTempMixItemDetailList>() {
                @Override
                public void onResponse(Call<GetTempMixItemDetailList> call, Response<GetTempMixItemDetailList> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("MIXING DETAIL: ", " - " + response.body());
                            mixDetailList.clear();
                            mixDetailList=response.body().getTempMixItemDetail();

                            for(int i=0;i<mixDetailList.size();i++)
                            {
                                for(int j=0;j<cotingCreamList.size();j++)
                                {
                                    if(cotingCreamList.get(j).getRmId()==mixDetailList.get(i).getRmId())
                                    {
                                        cotingCreamList.get(j).setTotal(cotingCreamList.get(j).getTotal()+mixDetailList.get(i).getTotal());
                                    }
                                }

                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            for(int i=0;i<cotingCreamList.size();i++) {
                                MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,cotingCreamList.get(i).getRmId(),cotingCreamList.get(i).getRmName(),cotingCreamList.get(i).getTotal(),cotingCreamList.get(i).getTotal(),sdf.format(System.currentTimeMillis()),0,0,0,"","","",0,cotingCreamList.get(i).getUom(),0,cotingCreamList.get(i).getTotal(),cotingCreamList.get(i).getEditTotal());
                                mixList.add(mixingDetailedSave);
                            }

                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),0,prodPlanHeader.getProductionBatch(),2,0,prodPlanHeader.getTimeSlot(),0,0,0,0,"","","",0,mixList);
                            saveMixing(mixingHeaderDetail,prodPlanHeader);
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetTempMixItemDetailList> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveMixing(MixingHeaderDetail mixingHeaderDetail, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING SAVE--------------------------"+mixingHeaderDetail);

        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<MixingHeaderDetail> listCall = Constants.myInterface.insertMixingHeaderndDetailed(mixingHeaderDetail);
            listCall.enqueue(new Callback<MixingHeaderDetail>() {
                @Override
                public void onResponse(Call<MixingHeaderDetail> call, Response<MixingHeaderDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE : ", " ------------------------------SAVE  MIXING------------------------ " + response.body());
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getupdateisMixingandBom(prodPlanHeader.getProductionHeaderId(),0,frItemStockConfiguresList.get(i).getSettingValue());
                            }
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
                public void onFailure(Call<MixingHeaderDetail> call, Throwable t) {
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

                            if (sfItemTempList != null) {
                                billDetailList.clear();
                                for (int i = 0; i < sfItemTempList.size(); i++) {
                                    BillOfMaterialDetailed billOfMaterialDetailed = new BillOfMaterialDetailed(0, 0, sfItemTempList.get(i).getRmType(), sfItemTempList.get(i).getRmId(), sfItemTempList.get(i).getRmName(), "", sfItemTempList.get(i).getRmQty(), 0, 0, 0, "", "", "", 0, 0, 0, sfItemTempList.get(i).getRmQty(), 0, 0);
                                    billDetailList.add(billOfMaterialDetailed);
                                }
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(),sdf.format(System.currentTimeMillis()),1,fromId,fromName,toId,toName,0,sdf.format(System.currentTimeMillis()),0,sdf.format(System.currentTimeMillis()),0,0,0,0,0,"","",prodPlanHeader.getIsPlanned(),0,0,sdf.format(System.currentTimeMillis()),0,sdf.format(System.currentTimeMillis()),billDetailList);
                            saveDetail(billOfMaterialHeader);
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

    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION MATERIAL HEADER--------------------------"+billOfMaterialHeader);

        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
//            commonDialog.show();

            Call<BillOfMaterialHeader> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<BillOfMaterialHeader>() {
                @Override
                public void onResponse(Call<BillOfMaterialHeader> call, Response<BillOfMaterialHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " ------------------------------SAVE PRODUCTION HEADER------------------------ " + response.body());
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity) context;
                            FragmentTransaction ft =activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new BMSListFragment(), "MainFragment");
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
                public void onFailure(Call<BillOfMaterialHeader> call, Throwable t) {
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

    private void getSettingKey(String dept) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+dept);
        if (Constants.isOnline(context)) {
          final CommonDialog  commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(dept);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE BMS: ", " - " + response.body());
                            // frItemStockConfiguresList.clear();
                            Configure configure=response.body();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();

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

}
