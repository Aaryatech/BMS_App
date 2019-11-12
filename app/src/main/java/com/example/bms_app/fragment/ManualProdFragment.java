package com.example.bms_app.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.manualAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.MixingDetailedSave;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.Production;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.Type;
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
public class ManualProdFragment extends Fragment implements View.OnClickListener{

public EditText edQtyKg;
private TextView tvProdNo,tvDate;
ProdPlanHeader prodPlanHeader;
Button btnSubmit,btnSerach;
RecyclerView recyclerView;
TextView tvTypeId,tvType,tvProduct,tvProductId;

Dialog dialog;
int typeId;
CommonDialog commonDialog, commonDialog1;
private List<FrItemStockConfigure> frItemStockConfiguresList;
private List<SfItemDetail> sfItemDetailList = new ArrayList<>();
ArrayList<BillOfMaterialDetailed> billDetailList = new ArrayList<>();
TypeListDialogAdapter typeAdapter;
ProductionListDialogAdapter productionAdapter;
List<Type> typeList=new ArrayList<>();
List<Type> typeArray = new ArrayList<>();
List<Production> productionList=new ArrayList<>();
List<Production> detailList=new ArrayList<>();
ArrayList<MixingDetailedSave> mixList = new ArrayList<>();
int bmsId=0;
String bmsName,toName;
Login loginUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manual_prod, container, false);
        getActivity().setTitle("Manual MIX & BOM");
        edQtyKg=view.findViewById(R.id.edQtyKg);
        tvProdNo=view.findViewById(R.id.tvProdNo);
        tvDate=view.findViewById(R.id.tvDate);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        btnSerach=view.findViewById(R.id.btnSerach);
        recyclerView=view.findViewById(R.id.recyclerView);

        tvTypeId=view.findViewById(R.id.tvTypeId);
        tvType=view.findViewById(R.id.tvType);
        tvProduct=view.findViewById(R.id.tvProduct);
        tvProductId=view.findViewById(R.id.tvProductId);

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

        getDeptSettingValueProduct("BMS");
//        getTypeList(0);


        tvType.setOnClickListener(this);
        tvProduct.setOnClickListener(this);
        btnSerach.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    private void getDeptSettingValueProduct(String bms) {
            Log.e("PARAMETER","                 SETTING KEY VALUES     "+bms);
            if (Constants.isOnline(getActivity())) {
                final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
                commonDialog.show();

                Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(bms);
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
                                        bmsId=frItemStockConfiguresList.get(0).getSettingValue();
                                        bmsName=frItemStockConfiguresList.get(0).getSettingKey();
                                        //}
                                    }
                                }

                                getTypeList(0,bmsId);

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

    private void getTypeList(int delStatus, final int bmsId) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Type>> listCall = Constants.myInterface.getSfType(delStatus);
            listCall.enqueue(new Callback<ArrayList<Type>>() {
                @Override
                public void onResponse(Call<ArrayList<Type>> call, Response<ArrayList<Type>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("TYPE LIST : ", " - " + response.body());
                            typeArray.clear();
                            typeArray=response.body();

                            if(typeArray!=null)
                            {
                                for(int i=0;i<typeArray.size();i++)
                                {
                                    Log.e("TYPE LIST : ", " --------------------------for----------------- " + typeArray);
                                    Log.e("BMS LIST : ", " --------------------------for----------------- " + bmsId);
                                    if(typeArray.get(i).getDelStatus().equals(bmsId))
                                    {
                                        Log.e("TYPE LIST : ", " --------------------------Equal----------------- " + bmsId);
                                        typeList.add(typeArray.get(i));
                                    }
                                }
                            }

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Type>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvType)
        {
            showDialog();

        }else if(v.getId()==R.id.tvProduct)
        {
            showDialog1();
        }else if(v.getId()==R.id.btnSerach) {
            String strType, strPrduction, srtPrductionId, strKg;
            boolean isValidType = false, isValidPrduction = false, isValidKg = false;

            strType = tvType.getText().toString();
            strPrduction = tvProduct.getText().toString();
            srtPrductionId = tvProductId.getText().toString();
            strKg = edQtyKg.getText().toString();
            int prodId=0,qtyKg=0;
            try {
                 prodId = Integer.parseInt(srtPrductionId);
                qtyKg = Integer.parseInt(strKg);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if (strType.isEmpty()) {
                tvType.setError("required");
            } else {
                tvType.setError(null);
                isValidType = true;
            }
            if (strPrduction.isEmpty()) {
                tvProduct.setError("required");
            } else {
                tvProduct.setError(null);
                isValidPrduction = true;
            }
            if (strKg.isEmpty()) {
                edQtyKg.setError("required");
            } else if (strKg.equalsIgnoreCase("0")) {
                edQtyKg.setError("invalid number");
            } else {
                edQtyKg.setError(null);
                isValidKg = true;
            }

            if(isValidType && isValidPrduction && isValidKg)
            {

                //getSfItemDetailsForCreamPrep(prodId);
                getSfItemDetailsApp(prodId,qtyKg);

            }
        }else if(v.getId()==R.id.btnSubmit)
        {
            if (sfItemDetailList.size()!=0) {
                //if (sfItemDetailList != null) {

                    billDetailList.clear();
                    for (int i = 0; i < sfItemDetailList.size(); i++) {
                        String strData = sfItemDetailList.get(i).getRmName();
                        String[] arrayString = strData.split("#");
                        String name = arrayString[0];
                        String unit = arrayString[1];
                        BillOfMaterialDetailed billOfMaterialDetailed = new BillOfMaterialDetailed(0, 0, sfItemDetailList.get(i).getRmType(), sfItemDetailList.get(i).getRmId(), name, unit, sfItemDetailList.get(i).getRmQty(), sfItemDetailList.get(i).getRmQty(), 0, 0, "", "", "", 0, 0, 0, sfItemDetailList.get(i).getRmQty(), 0, 0);
                        billDetailList.add(billOfMaterialDetailed);
                   }
               // }


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

                BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(), prodDate, 1, bmsId, bmsName, bmsId, bmsName, loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), 4, 0, 0, prodPlanHeader.getCatId(), 0, "", "", prodPlanHeader.getIsPlanned(), 0, 0, sdf.format(System.currentTimeMillis()), 0, sdf.format(System.currentTimeMillis()), billDetailList);
                saveDetail(billOfMaterialHeader, prodPlanHeader);
            }else {
                Toast.makeText(getActivity(), "Please Search item....", Toast.LENGTH_SHORT).show();
            }
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
                            String srtPrdId = tvProductId.getText().toString().trim();
                            String srtEditQty = edQtyKg.getText().toString().trim();

                            int editQty=0,prodId=0;
                            try{
                                editQty= Integer.parseInt(srtEditQty);
                                prodId= Integer.parseInt(srtPrdId);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            for(int j=0;j<productionList.size();j++)
                            {
                                if(productionList.get(j).getSfId()==prodId)
                                {
                                    detailList.add(productionList.get(j));
                                }
                            }

                            for(int i=0;i<detailList.size();i++) {

                                MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,detailList.get(i).getSfId(),detailList.get(i).getSfName(),editQty,editQty,sdf.format(System.currentTimeMillis()),0,0,0,""+detailList.get(i).getMulFactor(),"","",0,"",0,editQty,editQty);
                                mixList.add(mixingDetailedSave);

                            }
                            Log.e("MYTAG","-----------------------------------PROD ID MIX------------------------"+prodPlanHeader.getProductionHeaderId());
                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),prodPlanHeader.getProductionHeaderId(),prodPlanHeader.getProductionBatch(),2,0,prodPlanHeader.getTimeSlot(),1,bmsId,1,0,"","","",0,mixList);
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
                            ft.replace(R.id.content_frame, new BMSListFragment(), "BMSListFragment");
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


    private void getSfItemDetailsApp(Integer rmId, float prodQty) {

        Log.e("PARAMETER","                 RM ID     "+rmId  +"               EDITED VALUES           "+prodQty);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<SfItemHeader> listCall = Constants.myInterface.getSfItemDetailsApp(rmId,prodQty);
            listCall.enqueue(new Callback<SfItemHeader>() {
                @Override
                public void onResponse(Call<SfItemHeader> call, Response<SfItemHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION : ", " ----------------------SF ITEM DETAIL----------------------- " + response.body());
                            sfItemDetailList.clear();
                            sfItemDetailList.addAll(response.body().getSfItemDetail());

                            Log.e("LIST : ", " ----------------------LIST----------------------- " + sfItemDetailList);
                            manualAdapter adapter = new manualAdapter(sfItemDetailList, getContext());
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

    private void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dilaog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        typeAdapter = new TypeListDialogAdapter(typeList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(typeAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (typeAdapter != null) {
                       filterDept(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterDept(String text) {
        ArrayList<Type> temp = new ArrayList();
        for (Type d : typeList) {
            if (d.getSfTypeName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        typeAdapter.updateList(temp);
    }

    public class TypeListDialogAdapter extends RecyclerView.Adapter<TypeListDialogAdapter.MyViewHolder> {

        private List<Type> typeList;
        private Context context;

        public TypeListDialogAdapter(List<Type> typeList, Context context) {
            this.typeList = typeList;
            this.context = context;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public TypeListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_group_list, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final TypeListDialogAdapter.MyViewHolder myViewHolder, int i) {
            final Type model = typeList.get(i);
            myViewHolder.tvName.setText(model.getSfTypeName());


            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    getSfItemDetailsApp(0,0);
                    tvProductId.setText("");
                    tvProduct.setText("");
                    edQtyKg.setText("0");

                    tvType.setText(""+model.getSfTypeName());
                    tvTypeId.setText(""+model.getId());
                    typeId= Integer.parseInt(tvTypeId.getText().toString());
                    getProduction(typeId);

                }
            });
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public void updateList(ArrayList<Type> list) {
            typeList = list;
            notifyDataSetChanged();
        }

    }

    private void getProduction(int typeId) {
        Log.e("PARAMETER","              TYPE ID     "+typeId);
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Production>> listCall = Constants.myInterface.getItemSfHeadersBySfType(typeId);
            listCall.enqueue(new Callback<ArrayList<Production>>() {
                @Override
                public void onResponse(Call<ArrayList<Production>> call, Response<ArrayList<Production>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("TYPE LIST : ", " - " + response.body());
                            productionList.clear();
                            productionList=response.body();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Production>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialog1() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dilaog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);



        productionAdapter = new ProductionListDialogAdapter(productionList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(productionAdapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (productionAdapter != null) {
                       // filterEmp(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

//    void filterEmp(String text) {
//        ArrayList<Item> temp = new ArrayList();
//        for (Item d : itemList) {
//            if (d.getItemName().toLowerCase().contains(text.toLowerCase())) {
//                temp.add(d);
//            }
//        }
//        //update recyclerview
//        productionAdapter.updateList(temp);
//    }


    public class ProductionListDialogAdapter extends RecyclerView.Adapter<ProductionListDialogAdapter.MyViewHolder> {

        private List<Production> productionList;
        private Context context;
        //public ArrayList<String> assignedItemIdArray;

        public ProductionListDialogAdapter(List<Production> productionList, Context context) {
            this.productionList = productionList;
            this.context = context;
            //this.assignedItemIdArray = assignedItemIdArray;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public ProductionListDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_group_list, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ProductionListDialogAdapter.MyViewHolder myViewHolder, int i) {
            final Production model = productionList.get(i);
            myViewHolder.tvName.setText(model.getSfName());
            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    tvProduct.setText(""+model.getSfName());
                    tvProductId.setText(""+model.getSfId());


                }
            });
        }


        @Override
        public int getItemCount() {
            return productionList.size();
        }

        public void updateList(ArrayList<Production> list) {
            productionList = list;
            notifyDataSetChanged();
        }

    }
}
