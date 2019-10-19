package com.example.bms_app.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.RequestForMixingAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.ProductionDetail;
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
public class RequestForMixingFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    List<ProdPlanHeader> productionList = new ArrayList<>();
   // ProdPlanHeader productionDetail;
    RequestForMixingAdapter adapter;
    private String slugName;

    long fromDateMillis, toDateMillis;
    int yyyy, mm, dd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_request_for_mixing, container, false);
        getActivity().setTitle("Request for Mixing");

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      //  sdf.format(System.currentTimeMillis())
        getProductionList( sdf.format(System.currentTimeMillis()), sdf.format(System.currentTimeMillis()));

        return view;
    }

    private void getProductionList(String fromDate, String toDate) {
        Log.e("PARAMETER","            FROM DATE       "+ fromDate        +"          TO DATE     " +   toDate );

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ProductionDetail> listCall = Constants.myInterface.getProdPlanHeader(fromDate,toDate);
            listCall.enqueue(new Callback<ProductionDetail>() {
                @Override
                public void onResponse(Call<ProductionDetail> call, Response<ProductionDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION LIST : ", " - " + response.body());
                            productionList.clear();
                            productionList = response.body().getProdPlanHeader();

                           // productionList.add(productionDetail);

                            Log.e("Production model","------------------------------------------"+productionList);

                            adapter = new RequestForMixingAdapter(productionList, getContext());
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
                public void onFailure(Call<ProductionDetail> call, Throwable t) {
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
        if(v.getId()==R.id.fab)
        {
            new FilterDialog(getContext()).show();
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
            tvFromDate.setText(currentDate);
            tvToDate.setText(currentDate);

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

            // tvFromDate.setText(DateFrom);

            Date ToDate = null;
            try {
                ToDate = formatter1.parse(currentDate);//catch exception
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String DateTo = formatter2.format(ToDate);

            //tvToDate.setText(DateTo);

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

                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date FromDate = null;
                    try {
                        FromDate = formatter1.parse(fromDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // String DateFrom = formatter1.format(FromDate);

                    Date ToDate = null;
                    try {
                        ToDate = formatter1.parse(toDate);//catch exception
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // String DateTo = formatter1.format(ToDate);
                    Log.e("From Date","----------------------------------------------"+FromDate);
                    Log.e("To Date","----------------------------------------------"+ToDate);

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

                        getProductionList(fromDate, toDate);

                        dismiss();
                    }
                    else {
                        //dismiss();
                        boolean isFromDate=false;

                        Log.e("Mytag","fromDate"+fromDate);
                        Log.e("Mytag","toDate"+toDate);

                        getProductionList(fromDate, toDate);

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
}
