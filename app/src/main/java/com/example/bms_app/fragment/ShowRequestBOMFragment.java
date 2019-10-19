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
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.RequestBOMAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfAllMaterialHeader;
import com.example.bms_app.model.ShowAllReq;
import com.example.bms_app.utils.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRequestBOMFragment extends Fragment {
    private RecyclerView recyclerView;

    List<BillOfAllMaterialHeader> showAllList = new ArrayList<>();
    RequestBOMAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_request_bom, container, false);
        getActivity().setTitle("Show Request BOM");
        recyclerView = view.findViewById(R.id.recyclerView);

        getShowRequestBOM();

        return view;
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

                            adapter = new RequestBOMAdapter(showAllList, getContext());
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
