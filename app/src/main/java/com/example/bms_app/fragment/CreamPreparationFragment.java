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
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.MixingDetailedSave;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.PostProductionPlanHeader;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.model.TempMixItemDetail;
import com.example.bms_app.model.TempMixing;
import com.example.bms_app.utils.CommonDialog;
import com.example.bms_app.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Login loginUser;
    PostProductionPlanHeader postProductionPlanHeader;

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

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("HOME_ACTIVITY : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        getSettingKey("BMS");
        getSettingValue("PROD");

        try {
            PostProdPlanHeaderwithDetailed(prodPlanHeader.getProductionHeaderId());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

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

    private void PostProdPlanHeaderwithDetailed(Integer productionHeaderId) {
        Log.e("PARAMETER","                 PROD ID     "+productionHeaderId);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<PostProductionPlanHeader> listCall = Constants.myInterface.PostProdPlanHeaderwithDetailed(productionHeaderId);
            listCall.enqueue(new Callback<PostProductionPlanHeader>() {
                @Override
                public void onResponse(Call<PostProductionPlanHeader> call, Response<PostProductionPlanHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE BMS: ", " - " + response.body());
                            // frItemStockConfiguresList.clear();
                            postProductionPlanHeader=response.body();


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
                public void onFailure(Call<PostProductionPlanHeader> call, Throwable t) {
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
                                detailList.get(i).setEditTotal(detailList.get(i).getTotal()/1000);

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
                sfItemTempList = new ArrayList<>();
                Log.e("MYTAG", "-----------------------------------DETAIL BIN---------------------"+detailList);
                Log.e("LIST : ", " ---------------------- TEMP----------------------- " + sfItemTempList);

                for(int i=0;i<detailList.size();i++)
                {
                    if(detailList.get(i).getChecked())
                    {
                        getSfItemDetailsApp(detailList.get(i).getRmId(),detailList.get(i).getEditTotal());
                    }
                }
            }else if(v.getId()==R.id.btnSubmit)
            {
                if (sfItemTempList.size()!=0) {
                    Log.e("MYTAG", "-----------------------------------NULL---------------------");
                    billDetailList.clear();
                    for (int i = 0; i < sfItemTempList.size(); i++) {
                        Log.e("MYTAG", "-----------------------------------FOR---------------------");
                        String strData = sfItemTempList.get(i).getRmName();
                        String[] arrayString = strData.split("#");
                        String name = arrayString[0];
                        String unit = arrayString[1];
                        BillOfMaterialDetailed billOfMaterialDetailed = new BillOfMaterialDetailed(0, 0, sfItemTempList.get(i).getRmType(), sfItemTempList.get(i).getRmId(), name, unit, sfItemTempList.get(i).getRmQty(), sfItemTempList.get(i).getRmQty(), 0, 0, "", "", "", 0, 0, 0, sfItemTempList.get(i).getRmQty(), 0, 0);
                        billDetailList.add(billOfMaterialDetailed);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date FromDate = null;
                    try {
                        FromDate = formatter1.parse(prodPlanHeader.getProductionDate());//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String prodDate = sdf1.format(FromDate);
                    Log.e("MYTAG", "-----------------------------------DATE------------------------" + prodDate);
                    Log.e("MYTAG", "-----------------------------------PROD ID------------------------" + prodPlanHeader.getProductionHeaderId());

                    BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(), prodDate, 1, toId, toName, toId, toName, loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), 4, 0, 0, prodPlanHeader.getCatId(), 0, "", "", prodPlanHeader.getIsPlanned(), 0, 0, sdf.format(System.currentTimeMillis()), 0, sdf.format(System.currentTimeMillis()), billDetailList);
                    saveDetail(billOfMaterialHeader, prodPlanHeader);
                }else {
                    Toast.makeText(getActivity(), "Please add item first....", Toast.LENGTH_SHORT).show();
                }

//                for(int i=0;i<detailList.size();i++) {
//                    TempMixing model = new TempMixing(0, 0,detailList.get(i).getRmId(),detailList.get(i).getTotal(),prodPlanHeader.getProductionHeaderId());
//                    tempMixingDetailList.add(model);
//                }
//
             //  saveTempMixing(tempMixingDetailList,prodPlanHeader);
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

                            Log.e("SAVE MIXING : ", " ------------------------------SAVE  MIXING------------------------ " + response.body());
                            Toast.makeText(getActivity(), "Record Submitted Successfully....", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new CreamPreparationFragment(), "MainFragment");
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


    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION MATERIAL HEADER--------------------------"+billOfMaterialHeader+"           PROD ID       "+prodPlanHeader);

        if (Constants.isOnline(getActivity())) {
             commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<Info> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER BOM : ", " ------------------------------SAVE PRODUCTION HEADER------------------------ " + response.body());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            for(int i=0;i<detailList.size();i++) {
                                if(detailList.get(i).getChecked())
                                {
                                    MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,detailList.get(i).getRmId(),detailList.get(i).getRmName(),detailList.get(i).getEditTotal(),detailList.get(i).getEditTotal(),sdf.format(System.currentTimeMillis()),0,0,0,""+detailList.get(i).getSingleCut(),"","",0,detailList.get(i).getUom(),0,((detailList.get(i).getTotal()/1000)*detailList.get(i).getSingleCut()),((detailList.get(i).getTotal()/1000)*detailList.get(i).getSingleCut()));
                                    mixList.add(mixingDetailedSave);
                                }

                            }
                            Log.e("MYTAG","-----------------------------------PROD ID MIX------------------------"+prodPlanHeader.getProductionHeaderId());
                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),prodPlanHeader.getProductionHeaderId(),prodPlanHeader.getProductionBatch(),2,0,postProductionPlanHeader.getTimeSlot(),1,toId,0,0,"","","",0,mixList);
                            saveMixing(mixingHeaderDetail,prodPlanHeader);
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
                public void onFailure(Call<Info> call, Throwable t) {
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

                            sfItemDetailList = new ArrayList<>();
                             List<SfItemDetail> tempList = new ArrayList<>();
                            tempList= response.body().getSfItemDetail();

                            for(int i=0;i<tempList.size();i++){
                                sfItemDetailList.add(tempList.get(i));
                            }

                           // sfItemTempList = new ArrayList<>();

                             for(int j=0;j<sfItemDetailList.size();j++)
                                {
                                    int flag=0;

                                   for(int k=0;k<sfItemTempList.size();k++)
                                   {
                                       if(sfItemDetailList.get(j).getRmId()==sfItemTempList.get(k).getRmId() && sfItemDetailList.get(j).getRmType()==sfItemTempList.get(k).getRmType())
                                       {

                                           sfItemTempList.get(k).setRmQty(sfItemTempList.get(k).getRmQty()+sfItemDetailList.get(j).getRmQty());
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

                            Log.e("LIST : ", " ----------------------LIST ----------------------- " + sfItemDetailList);
                            Log.e("LIST : ", " ----------------------LIST TEMP----------------------- " + sfItemTempList);

                            // Log.e("DATA : ", "--------------------------DATA---------------------------"+sfItemTempList);
                            adapter1 = new CreamPreparationDetailAdapter(sfItemTempList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerViewDetail.setLayoutManager(mLayoutManager);
                            recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewDetail.setAdapter(adapter1);
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                           // Log.e("Data Null : ", "-----------");
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
