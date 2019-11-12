package com.example.bms_app.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.IssueFromBMSAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfAllMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.ShowAllReq;
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
public class IssueFromBMSFragment extends Fragment {
    private RecyclerView recyclerView;

    List<BillOfAllMaterialHeader> showAllList = new ArrayList<>();
    List<BillOfAllMaterialHeader> showAllPendingList = new ArrayList<>();
    IssueFromBMSAdapter adapter;

    private List<FrItemStockConfigure> frItemStockConfiguresList;;
    List<BillOfAllMaterialHeader> showAllFilterList = new ArrayList<>();

    int fromId=0,toId=0;
    String fromName,toName;

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_issue_from_bm, container, false);
        getActivity().setTitle("Issue from BMS");
        recyclerView = view.findViewById(R.id.recyclerView);
        getSettingValue("PROD");
        getShowRequestBOM();
        return view;
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

                                    Log.e("FROM ID","--------------------------------------------------"+fromId);
                                    Log.e("FROM NAME","--------------------------------------------------"+fromName);
                                    //}
                                }
                            }
                            getProductionSetting("BMS");
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

    private void getProductionSetting(final String dept) {
        Log.e("PARAMETER","                 SETTING KEY DEPT     "+dept);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(dept);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE MIX: ", " - " + response.body());
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

                                    Log.e("TO ID","--------------------------------------------------"+toId);
                                    Log.e("TO NAME","--------------------------------------------------"+toName);
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



    private void getShowRequestBOM() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ShowAllReq> listCall = Constants.myInterface.getallBOMHeaderList();
            listCall.enqueue(new Callback<ShowAllReq>() {
                @Override
                public void onResponse(Call<ShowAllReq> call, Response<ShowAllReq> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SHOW REQ BOM LIST : ", " - " + response.body());
                            showAllList.clear();
                            showAllList = response.body().getBillOfAllMaterialHeader();

                            // productionList.add(productionDetail);

                            Log.e("Production model","------------------------------------------"+showAllList);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            for(int i=0;i<showAllList.size();i++)
                            {
                                if((showAllList.get(i).getStatus()==0 && showAllList.get(i).getToDeptId()==toId) ||
                                        (showAllList.get(i).getReqDate().equalsIgnoreCase(sdf.format(System.currentTimeMillis())) && showAllList.get(i).getToDeptId()==toId)) {
                                    showAllPendingList.add(showAllList.get(i));
                                }
                            }
                            Log.e("Production model","--------------------Pending----------------------"+showAllPendingList);

                            adapter = new IssueFromBMSAdapter(showAllPendingList, getContext());
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
                public void onFailure(Call<ShowAllReq> call, Throwable t) {
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                new FilterDialog(getContext()).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class FilterDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;
        ImageView ivClose;
        CardView cardView;

        public FilterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_filter);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edFromDate);
            edToDate = findViewById(R.id.edTodate);
            tvFromDate = findViewById(R.id.tvFromDate);
            tvToDate = findViewById(R.id.tvToDate);
            Button btnFilter = findViewById(R.id.btnFilter);
            ivClose = findViewById(R.id.ivClose);


            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDate = formatter.format(todayDate);
            Log.e("Mytag","todayString"+currentDate);

            edToDate.setText(currentDate);
            edFromDate.setText(currentDate);

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

            Date FromDate = null;
            try {
                FromDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateFrom = formatter2.format(FromDate);

            tvFromDate.setText(DateFrom);

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateTo = formatter2.format(ToDate);

            tvToDate.setText(DateTo);

            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();


                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    int yr, mn, dy;
//                    if (toDateMillis > 0) {
//                        Calendar purchaseCal = Calendar.getInstance();
//                        purchaseCal.setTimeInMillis(toDateMillis);
//                        yr = purchaseCal.get(Calendar.YEAR);
//                        mn = purchaseCal.get(Calendar.MONTH);
//                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
//                    } else {
//                        Calendar purchaseCal = Calendar.getInstance();
//                        yr = purchaseCal.get(Calendar.YEAR);
//                        mn = purchaseCal.get(Calendar.MONTH);
//                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
//                    }
//                    DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
//                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                    dialog.show();

                    int yr, mn, dy;

                    long minValue = 0;

                    Calendar purchaseCal;

                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                    String fromDate = edFromDate.getText().toString().trim();
                    Date fromdate = null;

                    try {
                        fromdate = formatter1.parse(fromDate);//catch exception
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    purchaseCal = Calendar.getInstance();
                    purchaseCal.add(Calendar.DAY_OF_MONTH, -7);
                    minValue = purchaseCal.getTime().getTime();
                    purchaseCal.setTimeInMillis(toDateMillis);
                    yr = purchaseCal.get(Calendar.YEAR);
                    mn = purchaseCal.get(Calendar.MONTH);
                    dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, yr, mn, dy);
                    dialog.getDatePicker().setMinDate(fromdate.getTime());
                    //dialog.getDatePicker().setMinDate(purchaseCal.getTimeInMillis());
                    dialog.show();

                }
            });


            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String fromDate = tvFromDate.getText().toString();
                    String toDate = tvToDate.getText().toString();
                    SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date FromDate = null;
                    try {
                        FromDate = formatter1.parse(fromDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String DateFrom = formatter2.format(FromDate);

                    Date ToDate = null;
                    try {
                        ToDate = formatter1.parse(toDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String DateTo = formatter2.format(ToDate);


                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    }
//                    else if (dfDate.parse(fromDate).after(dfDate.parse(toDate))){
//                                edFromDate.setError("From Date must be less than To Date ");
//                                edFromDate.requestFocus();
//                     }
                    else if((FromDate.after(ToDate)))
                    {
                        edFromDate.setError("From Date must be less than To Date ");
                        edFromDate.requestFocus();
                    }else if(FromDate.equals(ToDate))
                    {
                        Log.e("Mytag Equal","fromDate"+fromDate);
                        Log.e("Mytag Equal","toDate"+toDate);

                        getAllRequestFilter(fromDate, toDate,fromId,toId);

                        dismiss();
                    }
                    else {
                        //dismiss();
                        boolean isFromDate=false;

                        Log.e("Mytag","fromDate"+fromDate);
                        Log.e("Mytag","toDate"+toDate);

                        getAllRequestFilter(fromDate, toDate,fromId,toId);

                        dismiss();

                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

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
                tvFromDate.setText(""+sdf.format(calendar.getTime()));
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
                tvToDate.setText(""+sdf.format(calendar.getTime()));
            }
        };
    }
    private void getAllRequestFilter(String fromDate, String toDate,int fromId, int toId) {
        Log.e("PARAMETER","          FROM DATE        "+fromDate+"            TO DATE          "+toDate+"            FROM ID       "+ fromId        +"          TO ID     " +   toId );

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ShowAllReq> listCall = Constants.myInterface.getBOMHeaderListBmsAndStore(fromDate,toDate,fromId,toId);
            listCall.enqueue(new Callback<ShowAllReq>() {
                @Override
                public void onResponse(Call<ShowAllReq> call, Response<ShowAllReq> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SHOW ALL LIST : ", " - " + response.body());
                            showAllFilterList.clear();

                            showAllFilterList= response.body().getBillOfAllMaterialHeader();

                            Log.e("Production model","--------------------Filter----------------------"+showAllFilterList);

                            adapter = new IssueFromBMSAdapter(showAllFilterList, getContext());
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
                public void onFailure(Call<ShowAllReq> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

}
