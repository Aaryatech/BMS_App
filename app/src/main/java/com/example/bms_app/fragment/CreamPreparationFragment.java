package com.example.bms_app.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.CreamPreparationAdapter;
import com.example.bms_app.adapter.CreamPreparationDetailAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
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
import com.example.bms_app.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreamPreparationFragment extends Fragment implements View.OnClickListener {
    ProdPlanHeader prodPlanHeader;
    private List<FrItemStockConfigure> frItemStockConfiguresList;
     CommonDialog commonDialog,commonDialog1;
    private List<SfPlanDetailForMixing> detailList = new ArrayList<>();
    private List<SfItemDetail> sfItemDetailList = new ArrayList<>();
    CreamPreparationAdapter adapter;
    CreamPreparationDetailAdapter adapter1;
    private TextView tvProdNo,tvDate;
    private RecyclerView recyclerView,recyclerViewDetail;
    private Button btnAdd,btnSubmit;
    private CheckBox checkbox;

    ArrayList<TempMixing> tempMixingDetailList = new ArrayList<>();
    private List<TempMixItemDetail> mixDetailList = new ArrayList<>();
    ArrayList<MixingDetailedSave> mixList = new ArrayList<>();
    ArrayList<BillOfMaterialDetailed> billDetailList = new ArrayList<>();
    int fromId=0,toId=0;
    String fromName,toName;
    List<SfItemDetail> sfItemTempList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cream_preparation, container, false);
        getActivity().setTitle("Generate Mixing For Cream Preparation");
        tvProdNo=view.findViewById(R.id.tvProdNo);
        tvDate=view.findViewById(R.id.tvDate);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerViewDetail=view.findViewById(R.id.recyclerViewDetail);
        btnAdd=view.findViewById(R.id.btnAdd);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        checkbox=view.findViewById(R.id.checkbox1);

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.PROD_ID);
            Gson gson = new Gson();
            prodPlanHeader = gson.fromJson(userStr, ProdPlanHeader.class);
            Log.e("HEDER : ", "--------------------------------------MODEL-----------------------------------" + prodPlanHeader);

            tvProdNo.setText("Prod Id : "+prodPlanHeader.getProductionHeaderId());
            tvDate.setText("Prod Date : "+prodPlanHeader.getProductionDate());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getSettingKey("BMS");
        getSettingValue("PROD");

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Log.e("LIST","------------------------"+detailList);
                    for(int k=0;k<detailList.size();k++)
                    {
                        Log.e("LIST SET","------------------------"+detailList.get(k));
                        detailList.get(k).setChecked(true);

                    }
                }else{
                    for(int k=0;k<detailList.size();k++)
                    {
                        Log.e("LIST SET","------------------------"+detailList.get(k));
                        detailList.get(k).setChecked(false);

                    }
                }

                adapter = new CreamPreparationAdapter(detailList, getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

            }
        });

        btnAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    private void getSettingKey(String dept) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+dept);
        if (Constants.isOnline(getActivity())) {
            commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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

                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getCreamPreparation(prodPlanHeader.getProductionHeaderId(), configure.getFrItemStockConfigure().get(i).getSettingValue());
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
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCreamPreparation(Integer productionHeaderId, final Integer deptId) {
        Log.e("PARAMETER","                 HEADER ID     "+productionHeaderId  +"               SETTING VALUES           "+deptId);
        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<DeptDetail> listCall = Constants.myInterface.showDetailsForCp(productionHeaderId,deptId);
            listCall.enqueue(new Callback<DeptDetail>() {
                @Override
                public void onResponse(Call<DeptDetail> call, Response<DeptDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION : ", " ----------------------Cream Preparation----------------------- " + response.body());
                            detailList.clear();
                            detailList=response.body().getSfPlanDetailForMixing();
                            for(int i=0;i<detailList.size();i++)
                            {
                                detailList.get(i).setChecked(false);
                                detailList.get(i).setEditTotal(detailList.get(i).getTotal());
                            }

                            adapter = new CreamPreparationAdapter(detailList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DeptDetail> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.btnAdd)
            {
                for(int i=0;i<detailList.size();i++)
                {
                    if(detailList.get(i).getChecked())
                    {
                        getSfItemDetailsApp(detailList.get(i).getRmId(),detailList.get(i).getEditTotal());
                    }
                }
            }else if(v.getId()==R.id.btnSubmit)
            {
                for(int i=0;i<detailList.size();i++) {
                    TempMixing model = new TempMixing(0, 0,detailList.get(i).getRmId(),detailList.get(i).getTotal(),prodPlanHeader.getProductionHeaderId());
                    tempMixingDetailList.add(model);
                }

                saveTempMixing(tempMixingDetailList,prodPlanHeader);
            }
    }


    private void saveTempMixing(ArrayList<TempMixing> tempMixing, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING TEMP--------------------------"+tempMixing);

        if (Constants.isOnline(getActivity())) {
            commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTempMixItemDetail(Integer productionHeaderId, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId);
        if (Constants.isOnline(getActivity())) {
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
                                for(int j=0;j<detailList.size();j++)
                                {
                                    if(detailList.get(j).getRmId()==mixDetailList.get(i).getRmId())
                                    {
                                        detailList.get(j).setTotal(detailList.get(j).getTotal()+mixDetailList.get(i).getTotal());
                                    }
                                }

                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            for(int i=0;i<detailList.size();i++) {
                                MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,detailList.get(i).getRmId(),detailList.get(i).getRmName(),detailList.get(i).getTotal(),detailList.get(i).getTotal(),sdf.format(System.currentTimeMillis()),0,0,0,"","","",0,detailList.get(i).getUom(),0,detailList.get(i).getTotal(),detailList.get(i).getEditTotal());
                                mixList.add(mixingDetailedSave);
                            }

                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),0,prodPlanHeader.getProductionBatch(),2,0,prodPlanHeader.getTimeSlot(),0,0,0,0,"","","",0,mixList);
                            saveMixing(mixingHeaderDetail,prodPlanHeader);
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetTempMixItemDetailList> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveMixing(MixingHeaderDetail mixingHeaderDetail, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING SAVE--------------------------"+mixingHeaderDetail);

        if (Constants.isOnline(getActivity())) {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getupdateisMixingandBom(Integer productionHeaderId, int flag, Integer dept) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId+"      FLAG      "+flag+"          DEPT            "+dept);
        if (Constants.isOnline(getActivity())) {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION MATERIAL HEADER--------------------------"+billOfMaterialHeader);

        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
//            commonDialog.show();

            Call<BillOfMaterialHeader> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<BillOfMaterialHeader>() {
                @Override
                public void onResponse(Call<BillOfMaterialHeader> call, Response<BillOfMaterialHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " ------------------------------SAVE PRODUCTION HEADER------------------------ " + response.body());
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new BMSListFragment(), "MainFragment");
                            ft.commit();
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                            builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    builder.setTitle("" + getActivity().getResources().getString(R.string.app_name));
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
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSettingValue(String prod) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+prod);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
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
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }







    private void getSfItemDetailsApp(Integer rmId, float editTotal) {
        Log.e("PARAMETER","                 RM ID     "+rmId  +"               EDITED VALUES           "+editTotal);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<SfItemHeader> listCall = Constants.myInterface.getSfItemDetailsApp(rmId,editTotal);
            listCall.enqueue(new Callback<SfItemHeader>() {
                @Override
                public void onResponse(Call<SfItemHeader> call, Response<SfItemHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION : ", " ----------------------SF ITEM DETAIL----------------------- " + response.body());
                           // sfItemDetailList.clear();
                            sfItemDetailList.addAll(response.body().getSfItemDetail());

                            Log.e("LIST : ", " ----------------------LIST----------------------- " + sfItemDetailList);

                             //List<SfItemDetail> sfItemTempList = new ArrayList<>();
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


                            adapter1 = new CreamPreparationDetailAdapter(sfItemTempList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerViewDetail.setLayoutManager(mLayoutManager);
                            recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewDetail.setAdapter(adapter1);
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SfItemHeader> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }
}
