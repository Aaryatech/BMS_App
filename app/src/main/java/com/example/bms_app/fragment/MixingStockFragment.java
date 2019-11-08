package com.example.bms_app.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.StockAdapter;
import com.example.bms_app.adapter.StockSFAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BmsCurrentStock;
import com.example.bms_app.model.BmsStockDetailed;
import com.example.bms_app.model.BmsStockHeader;
import com.example.bms_app.model.CurrentBmsSFStock;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Stock;
import com.example.bms_app.model.StockDetail;
import com.example.bms_app.model.StockDetailSf;
import com.example.bms_app.model.UpdateBmsSfStock;
import com.example.bms_app.model.UpdateBmsSfStockList;
import com.example.bms_app.model.UpdateBmsStock;
import com.example.bms_app.model.UpdateBmsStockList;
import com.example.bms_app.utils.CommonDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MixingStockFragment extends Fragment implements View.OnClickListener {
public Spinner spType,spOption;
public TextInputLayout tlFromMonth,tlToMonth;
public Button btnSearch;
public RecyclerView recyclerView;
public Button btnDayEndProcess;
public TextView tvDate;


public TextView tvFromMonth,tvToMonth;
public EditText edFromMonth,edToMonth;
public LinearLayout linearLayoutMonth,linearLayoutDate;

public TextInputLayout tlFromDate,tlToDate;
public TextView tvFromDate,tvToDate;
public EditText edFromDate,edToDate;

    CommonDialog commonDialog,commonDialog1;


    ArrayList<String> typeArray = new ArrayList<>();
    ArrayList<String> optionArray = new ArrayList<>();

    List<UpdateBmsStock> updateStockList = new ArrayList<>();
    List<UpdateBmsSfStock> updateBmsSfStockList = new ArrayList<>();
    List<BmsStockDetailed> bmsStockDetailList = new ArrayList<>();

    List<BmsCurrentStock> stockList = new ArrayList<>();
    Stock stock;

    List<CurrentBmsSFStock> stockSFList = new ArrayList<>();
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    String spinnerName;

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;
    public static int strDept;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mixing_stock, container, false);

        spType=(Spinner)view.findViewById(R.id.spType);
        spOption=(Spinner)view.findViewById(R.id.spOption);

        linearLayoutMonth=(LinearLayout) view.findViewById(R.id.linearLayoutMonth);
        linearLayoutDate=(LinearLayout)view.findViewById(R.id.linearLayoutDate);

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);

        tlFromMonth=(TextInputLayout) view.findViewById(R.id.tlFromMonth);
        tlToMonth=(TextInputLayout)view.findViewById(R.id.tlToMonth);
        tvFromMonth=(TextView) view.findViewById(R.id.tvFromMonth);
        tvToMonth=(TextView)view.findViewById(R.id.tvToMonth);
        edFromMonth=(EditText) view.findViewById(R.id.edFromMonth);
        edToMonth=(EditText)view.findViewById(R.id.edToMonth);

        tvDate=(TextView) view.findViewById(R.id.tvDate);

        tlFromDate=(TextInputLayout) view.findViewById(R.id.tlFromDate);
        tlToDate=(TextInputLayout)view.findViewById(R.id.tlToDate);
        tvFromDate=(TextView) view.findViewById(R.id.tvFromDate);
        tvToDate=(TextView)view.findViewById(R.id.tvToDate);
        edFromDate=(EditText) view.findViewById(R.id.edFromDate);
        edToDate=(EditText)view.findViewById(R.id.edToDate);

        btnSearch=(Button) view.findViewById(R.id.btnSearch);
        btnDayEndProcess=(Button) view.findViewById(R.id.btnDayEndProcess);

        try {
            tvDate.setText("Stock Date : "+ stock.getBmsStockDate());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            strDept = getArguments().getInt("strDept");

            Log.e("String", "--------------------------------DEPT-------------------------------------" + strDept);
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Exception","------------------------------"+e);
        }

        if(strDept==10)
        {
            typeArray.add("Semi Finished");
        }else if(strDept==11)
        {
            typeArray.add("Raw Material");
            typeArray.add("Semi Finished");
        }

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, typeArray);
        spType.setAdapter(spinnerAdapter);

        optionArray.add("Select Option");
        optionArray.add("Get Current Stock");
        optionArray.add("Get Stock Between Month");
        optionArray.add("Get Stock Between Date");


        final ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, optionArray);
        spOption.setAdapter(spinnerAdapter1);

        spOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = optionArray.get(position);
                if(spinnerName.equalsIgnoreCase("Get Current Stock"))
                {
                    linearLayoutMonth.setVisibility(View.GONE);
                    linearLayoutDate.setVisibility(View.GONE);
                  //  btnDayEndProcess.setVisibility(View.VISIBLE);
                }else if(spinnerName.equalsIgnoreCase("Get Stock Between Month"))
                {
                    linearLayoutMonth.setVisibility(View.VISIBLE);
                    linearLayoutDate.setVisibility(View.GONE);
                }else if(spinnerName.equalsIgnoreCase("Get Stock Between Date"))
                {
                    linearLayoutMonth.setVisibility(View.GONE);
                    linearLayoutDate.setVisibility(View.VISIBLE);
                    btnDayEndProcess.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edFromMonth.setOnClickListener(this);
        edToMonth.setOnClickListener(this);
        edFromDate.setOnClickListener(this);
        edToDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnDayEndProcess.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.edFromMonth)
        {
            int yr, mn, dy;
            if (fromDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), fromMonthListener, yr, mn, dy);
           // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        }else if(v.getId()==R.id.edToMonth)
        {
            int yr, mn, dy;
            if (fromDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), toMonthListener, yr, mn, dy);
            // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        } else if(v.getId()==R.id.edFromDate)
        {
            int yr, mn, dy;
            if (fromDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), fromDateListener, yr, mn, dy);
            // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        }else if(v.getId()==R.id.edToDate)
        {
            int yr, mn, dy;
            if (fromDateMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromDateMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
            // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        }else if(v.getId()==R.id.btnSearch)
        {
            int typeId = 0;;
           // int typeId = spType.getSelectedItemPosition();
            int optionId = spOption.getSelectedItemPosition();
            String strFromDate=edFromDate.getText().toString().trim();
            String strToDate=edFromDate.getText().toString().trim();
            Log.e("ID","---------------------------------------------TYPE---------------------------------"+typeId);
            Log.e("ID","---------------------------------------------OPTION---------------------------------"+optionId);

            if(strDept==10)
            {
                 typeId =2;

            }else if(strDept==11)
            {
                int type = spType.getSelectedItemPosition();
                typeId=type+1;
            }

            if(typeId==1 && optionId==1)
            {
                Log.e("TYPE1","-----------------------------------"+typeId);
                getBmsStockHeader(0,1,strDept);
            }else if(typeId==2 && optionId==1)
            {
                Log.e("TYPE2","-----------------------------------"+typeId);
                getBmsStockHeaderRaw(0,2,strDept);
            }else if(typeId==1 && optionId==3)
            {
                getBmsStockRMBetDate(strFromDate,strToDate);
            }else if(typeId==2 && optionId==3)
            {
                getBmsStockSFBetDate(strFromDate,strToDate);
            }

        }else if(v.getId()==R.id.btnDayEndProcess)
        {
            int typeId = spType.getSelectedItemPosition();
            if(typeId==0)
            {
               for(int i=0;i<stockList.size();i++)
               {
                   UpdateBmsStock updateBmsStock=new UpdateBmsStock(0,stockList.get(i).getRmId(),stock.getBmsStockId(),stockList.get(i).getProdIssueQty(),stockList.get(i).getProdRejectedQty(),stockList.get(i).getProdReturnQty(),stockList.get(i).getMixingIssueQty(),stockList.get(i).getMixingRejectedQty(),stockList.get(i).getMixingReturnQty(),stockList.get(i).getStoreIssueQty(),stockList.get(i).getStoreRejectedQty(),stockList.get(i).getBmsClosingStock(),0,0);
                   updateStockList.add(updateBmsStock);
               }
                UpdateBmsStockList updateBmsStockList=new UpdateBmsStockList();
                updateBmsStockList.setBmsStock(updateStockList);
                Log.e("UPDATE","---------------------------------------------UPDATE BMS---------------------------------"+updateBmsStockList);
                updateBmsStockForRM(updateBmsStockList);

                BmsStockHeader bmsStockHeader=new BmsStockHeader(stock.getBmsStockId(),stock.getBmsStockDate(),1,1,strDept,0,0,0,"",null);
                insertBmsStock(bmsStockHeader);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date stockDate1 = null;
                try {
                    stockDate1 = sdf.parse(stock.getBmsStockDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(stockDate1); // Now use today date.
                c.add(Calendar.DATE, 1); // Adding 1 day
                String stDate = sdf.format(c.getTime());
                Log.e("DATE","---------------------------------------------NEXT DATE---------------------------------"+stDate);
                try {
                    Date stockDate = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for(int i=0;i<stockList.size();i++)
                {
                    BmsStockDetailed bmsStockDetailed=new BmsStockDetailed(0,0,stDate,stock.getRmType(),stockList.get(i).getRmId(),stockList.get(i).getRmName(),stockList.get(i).getRmUomId(),stockList.get(i).getBmsClosingStock(),0,0,0,0,0,0,0,0,0,0,0,0,"");
                    bmsStockDetailList.add(bmsStockDetailed);
                }

                BmsStockHeader bmsStockHeader1=new BmsStockHeader(0,stDate,0,stock.getRmType(),strDept,0,0,0,"",bmsStockDetailList);
                insertBmsStock1(bmsStockHeader1);
            }else if(typeId==1)
            {
                for(int i=0;i<stockSFList.size();i++)
                {

                    UpdateBmsSfStock updateBmsSfStock=new UpdateBmsSfStock(0,stockSFList.get(i).getSfId(),stock.getBmsStockId(),stockSFList.get(i).getProdIssueQty(),stockSFList.get(i).getProdRejectedQty(),stockSFList.get(i).getProdReturnQty(),stockSFList.get(i).getMixingIssueQty(),stockSFList.get(i).getMixingRejectedQty(),stockSFList.get(i).getBmsClosingStock());
                    updateBmsSfStockList.add(updateBmsSfStock);
                }
                UpdateBmsSfStockList updateBmsSfStock=new UpdateBmsSfStockList();
                updateBmsSfStock.setBmsSfStock(updateBmsSfStockList);
                Log.e("UPDATE","---------------------------------------------UPDATE BMS---------------------------------"+updateBmsSfStockList);
                updateBmsStockForSF(updateBmsSfStock);

                BmsStockHeader bmsStockHeader=new BmsStockHeader(stock.getBmsStockId(),stock.getBmsStockDate(),1,stock.getRmType(),strDept,0,0,0,"",null);
                insertBmsStock(bmsStockHeader);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date stockDate1 = null;
                try {
                    stockDate1 = sdf.parse(stock.getBmsStockDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(stockDate1); // Now use today date.
                c.add(Calendar.DATE, 1); // Adding 1 day
                String stDate = sdf.format(c.getTime());
                Log.e("DATE","---------------------------------------------NEXT DATE---------------------------------"+stDate);
                try {
                    Date stockDate = sdf.parse(stDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for(int i=0;i<stockSFList.size();i++)
                {
                    BmsStockDetailed bmsStockDetailed=new BmsStockDetailed(0,0,stDate,stock.getRmType(),stockSFList.get(i).getSfId(),stockSFList.get(i).getSfName(),stockSFList.get(i).getSfUomId(),stockSFList.get(i).getBmsClosingStock(),0,0,0,0,0,0,0,0,0,0,0,0,"");
                    bmsStockDetailList.add(bmsStockDetailed);
                }

                BmsStockHeader bmsStockHeader1=new BmsStockHeader(0,stDate,0,stock.getRmType(),strDept,0,0,0,"",bmsStockDetailList);
                insertBmsStock1(bmsStockHeader1);
            }
        }

    }

    private void updateBmsStockForSF(UpdateBmsSfStockList updateBmsSfStock) {
        Log.e("PARAMETER","                  DETAIL  SF                  "+updateBmsSfStock);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<Info> listCall = Constants.myInterface.updateBmsStockForSF(updateBmsSfStock);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DETAIL : ", " ------------------------------UPDATE DETAIL SF------------------------ " + response.body());
                            // Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

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

    private void insertBmsStock(BmsStockHeader bmsStockHeader) {
        Log.e("PARAMETER","                  HEADER                    "+bmsStockHeader);
        if (Constants.isOnline(getActivity())) {
          final CommonDialog  commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<BmsStockHeader> listCall = Constants.myInterface.insertBmsStock(bmsStockHeader);
            listCall.enqueue(new Callback<BmsStockHeader>() {
                @Override
                public void onResponse(Call<BmsStockHeader> call, Response<BmsStockHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " -----------------------------8888888888-UPDATE HEADER-8888888----------------------- " + response.body());

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
                public void onFailure(Call<BmsStockHeader> call, Throwable t) {
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

    private void insertBmsStock1(BmsStockHeader bmsStockHeader) {
        Log.e("PARAMETER","                  HEADER                    "+bmsStockHeader);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog  commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<BmsStockHeader> listCall = Constants.myInterface.insertBmsStock(bmsStockHeader);
            listCall.enqueue(new Callback<BmsStockHeader>() {
                @Override
                public void onResponse(Call<BmsStockHeader> call, Response<BmsStockHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " -----------------------------8888888888-UPDATE HEADER-8888888----------------------- " + response.body());
                            Toast.makeText(getActivity(), "Record Submitted Successfully....", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new MixingStockFragment(), "Exit");
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
                public void onFailure(Call<BmsStockHeader> call, Throwable t) {
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

    private void updateBmsStockForRM(UpdateBmsStockList updateBmsStockList) {
        Log.e("PARAMETER","                  DETAIL                    "+updateBmsStockList);
        if (Constants.isOnline(getActivity())) {
           final CommonDialog commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<Info> listCall = Constants.myInterface.updateBmsStockForRM(updateBmsStockList);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DETAIL : ", " ------------------------------UPDATE DETAIL------------------------ " + response.body());
                           // Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

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

    private void getBmsStockSFBetDate(String strFromDate, String strToDate) {
        Log.e("PARAMETER","         FROM DATED     "+strFromDate+"            TO DATE    "+strToDate);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
           commonDialog.show();

            Call<StockDetailSf> listCall = Constants.myInterface.getBmsStockSFBetDate(strFromDate,strToDate);
            listCall.enqueue(new Callback<StockDetailSf>() {
                @Override
                public void onResponse(Call<StockDetailSf> call, Response<StockDetailSf> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Stock List : ", " - " + response.body());
                            stockSFList.clear();
                            stockSFList=response.body().getCurrentBmsSFStock();

                            Log.e("LIST","----------------------Stock list---------------"+stockSFList);

                            for(int i=0;i<stockSFList.size();i++)
                            {
                                stockSFList.get(i).setBmsClosingStock((stockSFList.get(i).getBmsOpeningStock() + stockSFList.get(i).getMixingIssueQty() - stockSFList.get(i).getProdIssueQty()));
                            }

                            StockSFAdapter adapter = new StockSFAdapter(stockSFList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

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
                public void onFailure(Call<StockDetailSf> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBmsStockRMBetDate(String strFromDate, String strToDate) {
        Log.e("PARAMETER","         FROM DATED     "+strFromDate+"            TO DATE    "+strToDate);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
           commonDialog.show();

            Call<StockDetail> listCall = Constants.myInterface.getBmsStockRMBetDate(strFromDate,strToDate);
            listCall.enqueue(new Callback<StockDetail>() {
                @Override
                public void onResponse(Call<StockDetail> call, Response<StockDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Stock List : ", " - " + response.body());
                            stockList.clear();
                            stockList=response.body().getBmsCurrentStock();

                            Log.e("LIST","----------------------Stock list---------------"+stockList);

                            for(int i=0;i<stockList.size();i++)
                            {
                                stockList.get(i).setBmsClosingStock((stockList.get(i).getBmsOpeningStock() + stockList.get(i).getMixingIssueQty() - stockList.get(i).getProdIssueQty()));
                            }

                            StockAdapter adapter = new StockAdapter(stockList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

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
                public void onFailure(Call<StockDetail> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBmsStockHeaderRaw(int status, int rmType, final int dept) {
        Log.e("PARAMETER","               STATUS            "+status+"            RM TYPE            "+rmType+"            DEPT                "+dept);
        if (Constants.isOnline(getActivity())) {
            commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

            Call<Stock> listCall = Constants.myInterface.getBmsStockHeader(status,rmType,dept);
            listCall.enqueue(new Callback<Stock>() {
                @Override
                public void onResponse(Call<Stock> call, Response<Stock> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("STOCK RESPONCE : ", " - " + response.body());
                             stock=response.body();
                            tvDate.setText("Stock Date : "+stock.getBmsStockDate());
                            Log.e("STOCK RESPONCE : ", " -----------------------------------------Bin---------------------- " + stock);

                            SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

                            if (dfDate.parse(dfDate.format(System.currentTimeMillis())).after(dfDate.parse(stock.getBmsStockDate()))){
                                btnDayEndProcess.setVisibility(View.VISIBLE);
                                Log.e("DATE : ", " -----------------------------------------DATE VISIBLE--------------------- ");

                            }else{
                                btnDayEndProcess.setVisibility(View.GONE);
                                Log.e("DATE : ", " -----------------------------------------DATE GONE--------------------- ");
                            }



//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                            Date stockDate = sdf.parse(stock.getBmsStockDate());
//                            if (new Date().after(stockDate)) {
//                                btnDayEndProcess.setVisibility(View.VISIBLE);
//                            }
//                            else{
//                                btnDayEndProcess.setVisibility(View.GONE);
//                            }

                            getCurentBmsStockSF(dept,stock.getBmsStockDate());

                            commonDialog1.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Stock> call, Throwable t) {
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


    private void getCurentBmsStockSF(int dept, String stockDate) {
        Log.e("PARAMETER","         DEPT ID     "+dept+"            BMS STOCK DATE    "+stockDate);
        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//           commonDialog.show();

            Call<StockDetailSf> listCall = Constants.myInterface.getCurentBmsStockSF(dept,stockDate);
            listCall.enqueue(new Callback<StockDetailSf>() {
                @Override
                public void onResponse(Call<StockDetailSf> call, Response<StockDetailSf> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Stock List : ", " - " + response.body());
                            stockSFList.clear();
                            stockSFList=response.body().getCurrentBmsSFStock();

                            Log.e("LIST","----------------------Stock list---------------"+stockSFList);

                            for(int i=0;i<stockSFList.size();i++)
                            {
                                stockSFList.get(i).setBmsClosingStock((stockSFList.get(i).getBmsOpeningStock() + stockSFList.get(i).getMixingIssueQty() - stockSFList.get(i).getProdIssueQty()));
                            }

                            StockSFAdapter adapter = new StockSFAdapter(stockSFList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<StockDetailSf> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBmsStockHeader(int status, int rmType, final int dept) {
        Log.e("PARAMETER","               STATUS            "+status+"            RM TYPE            "+rmType+"            DEPT                "+dept);
        if (Constants.isOnline(getActivity())) {
              commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Stock> listCall = Constants.myInterface.getBmsStockHeader(status,rmType,dept);
            listCall.enqueue(new Callback<Stock>() {
                @Override
                public void onResponse(Call<Stock> call, Response<Stock> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("STOCK RESPONCE : ", " - " + response.body());
                            // frItemStockConfiguresList.clear();
                             stock=response.body();
                            tvDate.setText("Stock Date : "+stock.getBmsStockDate());
                            Log.e("STOCK RESPONCE : ", " -----------------------------------------Bin---------------------- " + stock);

                    SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

                     if (dfDate.parse(dfDate.format(System.currentTimeMillis())).after(dfDate.parse(stock.getBmsStockDate()))){
                         btnDayEndProcess.setVisibility(View.VISIBLE);
                         Log.e("DATE : ", " -----------------------------------------DATE VISIBLE--------------------- ");

                     }else{
                         btnDayEndProcess.setVisibility(View.GONE);
                         Log.e("DATE : ", " -----------------------------------------DATE GONE--------------------- ");
                     }

                     getCurentBmsStockRM(dept,stock.getBmsStockDate());

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
                public void onFailure(Call<Stock> call, Throwable t) {
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

    private void getCurentBmsStockRM(int dept, String bmsStockDate) {
        Log.e("PARAMETER","         DEPT ID     "+dept+"            BMS STOCK DATE    "+bmsStockDate);
        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//           commonDialog.show();

            Call<StockDetail> listCall = Constants.myInterface.getCurentBmsStockRM(dept,bmsStockDate);
            listCall.enqueue(new Callback<StockDetail>() {
                @Override
                public void onResponse(Call<StockDetail> call, Response<StockDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Stock List : ", " - " + response.body());
                            stockList.clear();
                            stockList=response.body().getBmsCurrentStock();

                            Log.e("LIST","----------------------Stock list---------------"+stockList);

                            for(int i=0;i<stockList.size();i++)
                            {
                                stockList.get(i).setBmsClosingStock((stockList.get(i).getBmsOpeningStock() + stockList.get(i).getMixingIssueQty() - stockList.get(i).getProdIssueQty()));
                            }

                            StockAdapter adapter = new StockAdapter(stockList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

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
                public void onFailure(Call<StockDetail> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener fromMonthListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            edFromMonth.setText(""+sdf1.format(calendar.getTime()));
            tvFromMonth.setText(""+sdf1.format(calendar.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener toMonthListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;


            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            edToMonth.setText(""+sdf1.format(calendar.getTime()));
            tvToMonth.setText(""+sdf1.format(calendar.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            edFromDate.setText(""+sdf1.format(calendar.getTime()));
            tvFromDate.setText(""+sdf1.format(calendar.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;


            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

            edToDate.setText(""+sdf1.format(calendar.getTime()));
            tvToDate.setText(""+sdf1.format(calendar.getTime()));
        }
    };
}
