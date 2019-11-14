package com.example.bms_app.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.activity.MainActivity;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Login;
import com.example.bms_app.utils.CommonDialog;
import com.example.bms_app.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
public LinearLayout linearLayoutReqMix,linearLayoutReqStore,linearLayoutReqAll,linearLayoutAddBmsProd,linearLayoutBmsStock,linearLayoutBmsProdList,linearLayoutIssueBms,linearLayoutReqProd,linearLayoutAddMix,linearLayoutProdList,linearLayoutMixStock;
public Login loginUser;
private List<FrItemStockConfigure> frItemStockConfiguresList;
int bmsId=0,mixId=0;
String bmsName,mixName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Dashboard");

        linearLayoutReqMix=(LinearLayout) view.findViewById(R.id.linearLayoutReqMix);
        linearLayoutReqStore=(LinearLayout)view.findViewById(R.id.linearLayoutReqStore);
        linearLayoutReqAll=(LinearLayout)view.findViewById(R.id.linearLayoutReqAll);

        linearLayoutReqProd=(LinearLayout)view.findViewById(R.id.linearLayoutReqProd);
        linearLayoutAddMix=(LinearLayout)view.findViewById(R.id.linearLayoutAddMix);
        linearLayoutProdList=(LinearLayout)view.findViewById(R.id.linearLayoutProdList);
        linearLayoutMixStock=(LinearLayout)view.findViewById(R.id.linearLayoutMixStock);

        linearLayoutAddBmsProd=(LinearLayout)view.findViewById(R.id.linearLayoutAddBmsProd);
        linearLayoutBmsStock=(LinearLayout)view.findViewById(R.id.linearLayoutBmsStock);
        linearLayoutBmsProdList=(LinearLayout)view.findViewById(R.id.linearLayoutBmsProdList);
        linearLayoutIssueBms=(LinearLayout)view.findViewById(R.id.linearLayoutIssueBms);

        getSettingValue("MIX");
        getSettingValueBMS("BMS");

        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("HOME_ACTIVITY : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        linearLayoutReqMix.setOnClickListener(this);
        linearLayoutReqStore.setOnClickListener(this);
        linearLayoutReqAll.setOnClickListener(this);

        linearLayoutReqProd.setOnClickListener(this);
        linearLayoutAddMix.setOnClickListener(this);
        linearLayoutProdList.setOnClickListener(this);
        linearLayoutMixStock.setOnClickListener(this);

        linearLayoutAddBmsProd.setOnClickListener(this);
        linearLayoutBmsStock.setOnClickListener(this);
        linearLayoutBmsProdList.setOnClickListener(this);
        linearLayoutIssueBms.setOnClickListener(this);

        return view;
    }

    private void getSettingValue(String mix) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+mix);
        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(mix);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE MIX: ", " - " + response.body());

                            // frItemStockConfiguresList.clear();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            if(frItemStockConfiguresList!=null)
                            {
                                if(frItemStockConfiguresList.size()>0)
                                {
                                    // if(settingKey.equalsIgnoreCase("PROD"))
                                    // {
                                    mixId=frItemStockConfiguresList.get(0).getSettingValue();
                                    mixName=frItemStockConfiguresList.get(0).getSettingKey();
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

    private void getSettingValueBMS(String bms) {
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.linearLayoutReqMix)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new RequestForMixingFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutReqStore)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new RequestForStoreFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutReqAll)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ShowAllRequestFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutAddBmsProd)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new BMSListFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutBmsStock)
        {
            Fragment adf = new MixingStockFragment();
            Bundle args = new Bundle();
            args.putInt("strDept", bmsId);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
        }else if(v.getId()==R.id.linearLayoutBmsProdList)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new BMSProductionFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutIssueBms)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new IssueFromBMSFragment(), "MainFragment");
            ft.commit();
        }
        else if(v.getId()==R.id.linearLayoutReqProd)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ShowRequestBOMFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutAddMix)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MixingProductionFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutProdList)
        {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MixingProductionListFragment(), "MainFragment");
            ft.commit();
        }else if(v.getId()==R.id.linearLayoutMixStock)
        {
            Fragment adf = new MixingStockFragment();
            Bundle args = new Bundle();
            args.putInt("strDept", mixId);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_logout);
        MenuItem item1 = menu.findItem(R.id.action_Name);
        item1.setTitle(loginUser.getUser().getUsername());
        item.setVisible(true);
        item1.setVisible(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Name:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure ! you want to logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CustomSharedPreference.deletePreference(getActivity());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
