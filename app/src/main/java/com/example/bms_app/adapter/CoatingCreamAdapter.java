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
import com.example.bms_app.fragment.CotingCreamFragment;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.MixingDetailedSave;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.model.TempMixItemDetail;
import com.example.bms_app.model.TempMixing;
import com.example.bms_app.utils.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    Login loginUser;

    public CoatingCreamAdapter(List<SfPlanDetailForMixing> cotingCreamList, Context context,ProdPlanHeader prodPlanHeader, Login loginUser) {
        this.cotingCreamList = cotingCreamList;
        this.context = context;
        this.prodPlanHeader = prodPlanHeader;
        this.loginUser = loginUser;
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
        Log.e("Model","------------------------DOUBLE CUT--------------------------"+model.getDoubleCut());

        if(model.getRmType()==1)
        {
            myViewHolder.tvType.setText("RM");
        }else if(model.getRmType()==2)
        {
            myViewHolder.tvType.setText("SF");
        }

        if(model.getDoubleCut()==1.0)
        {
            Log.e("False","---------------------------------"+model.getDoubleCut());
            myViewHolder.btnBOM.setEnabled(false);
           // myViewHolder.btnBOM.setBackgroundColor(context.getResources().getColor(R.color.lightPink));
            myViewHolder.btnBOM.setBackgroundResource(R.drawable.rounded_corner_light_button);

        }else if(model.getDoubleCut()==0.0)
        {
            Log.e("True","---------------------------------"+model.getDoubleCut());
            myViewHolder.btnBOM.setEnabled(true);
           // myViewHolder.btnBOM.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            myViewHolder.btnBOM.setBackgroundResource(R.drawable.rounded_corner_button);
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
               // getSfItemDetailsForCreamPrep(model.getRmId(),model,prodPlanHeader);
                getSfItemDetailsApp(model.getRmId(),model.getTotal()/1000,model,prodPlanHeader);

            }
        });
    }

    private void getSfItemDetailsApp(Integer rmId, float prodQty, final SfPlanDetailForMixing model, final ProdPlanHeader prodPlanHeader) {

        Log.e("PARAMETER","                 RM ID     "+rmId  +"               EDITED VALUES           "+prodQty);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
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

                            new DeptDialog(context,model,sfItemDetailList,prodPlanHeader).show();
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
        public Button btnSubmit,btnClose;
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
            btnClose = (Button) findViewById(R.id.btnClose);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            getSettingValue("PROD");
            getSettingKey("BMS");

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                    if (sfItemTempList.size()!=0) {
                       // if (sfItemTempList != null) {
                            billDetailList.clear();
                            for (int i = 0; i < sfItemTempList.size(); i++) {
                                String strData = sfItemTempList.get(i).getRmName();
                                String[] arrayString = strData.split("#");
                                String name = arrayString[0];
                                String unit = arrayString[1];
                                BillOfMaterialDetailed billOfMaterialDetailed = new BillOfMaterialDetailed(0, 0, sfItemTempList.get(i).getRmType(), sfItemTempList.get(i).getRmId(), name, unit, sfItemTempList.get(i).getRmQty(), sfItemTempList.get(i).getRmQty(), 0, 0, "", "", "", 0, 0, 0, sfItemTempList.get(i).getRmQty(), 0, 0);
                                billDetailList.add(billOfMaterialDetailed);
                            }
                      //  }


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


                        BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(), prodDate, 1, toId, toName, toId, toName, loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), 4, 0, 0, prodPlanHeader.getCatId(), 0, "", "", prodPlanHeader.getIsPlanned(), 0, 0, sdf.format(System.currentTimeMillis()), 0, sdf.format(System.currentTimeMillis()), billDetailList);
                        saveDetail(billOfMaterialHeader, model);

//                        TempMixing tempMixing = new TempMixing(0, 0, model.getRmId(), model.getTotal(), prodPlanHeader.getProductionHeaderId());
//                        tempMixingDetailList.add(tempMixing);
//
                        //  saveTempMixing(tempMixingDetailList, prodPlanHeader);
                    }else {
                        Toast.makeText(context, "Please add item first....", Toast.LENGTH_SHORT).show();
                    }
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
                            Toast.makeText(context, "Record Submitted Successfully....", Toast.LENGTH_SHORT).show();
                            MainActivity activity=(MainActivity)context;
                            FragmentTransaction ft =activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new CotingCreamFragment(), "MainFragment");
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



    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader, final SfPlanDetailForMixing model) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION MATERIAL HEADER--------------------------"+billOfMaterialHeader+"                     MIXING DETAIL      "+model);

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
                          //  Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity) context;

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                         //   for(int i=0;i<cotingCreamList.size();i++) {
                                MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,model.getRmId(),model.getRmName(),model.getEditTotal(),model.getEditTotal(),sdf.format(System.currentTimeMillis()),0,0,0,""+model.getSingleCut(),"","",0,model.getUom(),0,((model.getTotal()/1000)*model.getSingleCut()),((model.getTotal()/1000)*model.getSingleCut()));
                                mixList.add(mixingDetailedSave);
                          //  }

                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),prodPlanHeader.getProductionHeaderId(),prodPlanHeader.getProductionBatch(),2,0,prodPlanHeader.getTimeSlot(),1,toId,0,0,"","","",0,mixList);
                            saveMixing(mixingHeaderDetail,prodPlanHeader);

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
