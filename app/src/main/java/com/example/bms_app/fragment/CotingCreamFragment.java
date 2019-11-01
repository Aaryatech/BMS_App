package com.example.bms_app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.CoatingCreamAdapter;
import com.example.bms_app.adapter.CreamPreparationDetailAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfItemDetail;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.utils.CommonDialog;
import com.example.bms_app.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CotingCreamFragment extends Fragment implements View.OnClickListener {
    ProdPlanHeader prodPlanHeader;
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    CommonDialog commonDialog;
    private List<SfPlanDetailForMixing> detailList = new ArrayList<>();
    private List<SfItemDetail> sfItemDetailList = new ArrayList<>();
    CoatingCreamAdapter adapter;
    CreamPreparationDetailAdapter adapter1;
    private TextView tvProdNo,tvDate;
    private RecyclerView recyclerView,recyclerViewDetail;
    Login loginUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_coting_cream, container, false);
        getActivity().setTitle("Generate Mixing For Coating");
        tvProdNo=view.findViewById(R.id.tvProdNo);
        tvDate=view.findViewById(R.id.tvDate);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerViewDetail=view.findViewById(R.id.recyclerViewDetail);


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


        return view;
    }

    @Override
    public void onClick(View v) {

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

    private void getCreamPreparation(Integer productionHeaderId, Integer deptId) {

        Log.e("PARAMETER","                 HEADER ID     "+productionHeaderId  +"               SETTING VALUES           "+deptId);
        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<DeptDetail> listCall = Constants.myInterface.showDetailsForCoating(productionHeaderId,deptId);
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
                                detailList.get(i).setEditTotal(detailList.get(i).getTotal()/1000);
                            }

                            adapter = new CoatingCreamAdapter(detailList, getContext(),prodPlanHeader,loginUser);
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
}
