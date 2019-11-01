package com.example.bms_app.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.IssuesAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Group;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Item;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.PostProductionPlanHeader;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.SfPlanDetailForMixing;
import com.example.bms_app.utils.CommonDialog;
import com.example.bms_app.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class IssusFragment extends Fragment implements View.OnClickListener{
    private TextView tvProdNo,tvDate;
    ProdPlanHeader prodPlanHeader;
    Button btnSubmit,btnSerach;
    TextView tvItemId,tvItem,tvGroup,tvGroupId;
    RecyclerView recyclerView;
    public RadioButton rbIssue,rbManual;
    public RadioGroup rgType;
    ArrayList<Group> groupList = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    ArrayList<SfPlanDetailForMixing> searchList = new ArrayList<>();
    List<Integer> list = new ArrayList<>();
    Login loginUser;
    int fromId=0,toId=0,type=0;
    String fromName,toName,selectedText;

    Dialog dialog;
    int groupId;
    CommonDialog commonDialog,commonDialog1;
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    GrouptListDialogAdapter groupAdapter;
    ItemListDialogAdapter itemAdapter;
    String stringId,stringName;
    public static ArrayList<Item> assignStaticItemList = new ArrayList<>();
    ArrayList<BillOfMaterialDetailed> billDetailList = new ArrayList<>();
    PostProductionPlanHeader postProductionPlanHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //IssuesAdapter
        View view= inflater.inflate(R.layout.fragment_issus, container, false);
        getActivity().setTitle("ISSUE RM");
        tvProdNo=view.findViewById(R.id.tvProdNo);
        tvDate=view.findViewById(R.id.tvDate);
        tvItemId=view.findViewById(R.id.tvItemId);
        tvItem=view.findViewById(R.id.tvItem);
        tvGroup=view.findViewById(R.id.tvGroup);
        tvGroupId=view.findViewById(R.id.tvGroupId);
        btnSubmit=view.findViewById(R.id.btnSubmit);
        btnSerach=view.findViewById(R.id.btnSerach);
        recyclerView=view.findViewById(R.id.recyclerView);

        rbIssue=view.findViewById(R.id.rbIssue);
        rbManual=view.findViewById(R.id.rbManual);
        rgType=view.findViewById(R.id.rgType);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                View radioButton = group.findViewById(radioButtonID);
                int idx = group.indexOfChild(radioButton);
                RadioButton r = (RadioButton) group.getChildAt(idx);
                selectedText = r.getText().toString();
                Log.e(" Radio", "----------" + idx);
                Log.e(" Radio Text", "----------" + selectedText);

                if(selectedText.equalsIgnoreCase("Issue"))
                {
                   type=0;

                }else if(selectedText.equalsIgnoreCase("Manual"))
                {
                    type=1;
                }
            }
        });


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

        getGroup();
       // getDeptSettingValue("BMS");
        getSettingKey("BMS","");
        getDeptSettingValueProduct("PROD");

        try {
            PostProdPlanHeaderwithDetailed(prodPlanHeader.getProductionHeaderId());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        rbIssue.setChecked(true);
        tvGroup.setOnClickListener(this);
        tvItem.setOnClickListener(this);
        btnSerach.setOnClickListener(this);
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

    private void getDeptSettingValueProduct(String prod) {
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


    private void getGroup() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<Group>> listCall = Constants.myInterface.showMiniSubCatList();
            listCall.enqueue(new Callback<ArrayList<Group>>() {
                @Override
                public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("DEPT LIST : ", " - " + response.body());
                            groupList.clear();
                            groupList=response.body();

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
                public void onFailure(Call<ArrayList<Group>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSerach) {
            // getSfDetailsForIssue
            String strGroup, strItem, srtItemId;
            boolean isValidGroup = false, isValidItem = false;

            strGroup = tvGroup.getText().toString();
            strItem = tvItem.getText().toString();
            srtItemId = tvItemId.getText().toString();

            if (strGroup.isEmpty()) {
                tvGroup.setError("required");
            } else {
                tvGroup.setError(null);
                isValidGroup = true;
            }

            if (strItem.isEmpty()) {
                tvItem.setError("required");
            } else {
                tvItem.setError(null);
                isValidItem = true;
            }

            if (isValidGroup && isValidItem) {
                getSettingKey("BMS", stringId);

            }

        } else if (v.getId() == R.id.tvGroup) {
            showDialog();
        } else if (v.getId() == R.id.tvItem) {
            showDialog1();
        } else if (v.getId() == R.id.btnSubmit) {
            String strItem = tvItem.getText().toString();
            String strItemId = tvItemId.getText().toString();
            int item = 0;
            try {
                item = Integer.parseInt(strItem);
            } catch (Exception e) {
                e.printStackTrace();
            }

            type = 0;
            if (rbIssue.isChecked()) {
                type = 0;
            } else if (rbManual.isChecked()) {
                type = 1;
            }

            if (searchList.size()!=0) {
               // if (searchList != null) {
                    // searchList.clear();
                    for (int i = 0; i < searchList.size(); i++) {
                        //  list.add(searchList.get(i).getItemId());
                        BillOfMaterialDetailed billOfMaterialDetailed = new BillOfMaterialDetailed(0, searchList.get(i).getItemDetailId(), searchList.get(i).getRmType(), searchList.get(i).getRmId(), searchList.get(i).getRmName(), searchList.get(i).getUom(), searchList.get(i).getEditTotal(), searchList.get(i).getEditTotal(), 1, 0, String.valueOf(searchList.get(i).getSingleCut()), String.valueOf(searchList.get(i).getDoubleCut()), "", 0, 0, 0, searchList.get(i).getTotal(), 0, 0);
                        billDetailList.add(billOfMaterialDetailed);
                    }

               // }

                Set<Integer> itemWithoutDuplicates = new HashSet<Integer>();
                for (int j = 0; j < searchList.size(); j++) {
                    itemWithoutDuplicates.add(searchList.get(j).getItemId());

                }


                Log.e("ASSIGN ITEM ID", "---------------------------------" + itemWithoutDuplicates);
                Log.e("SEARCH LIST", "---------------------------------" + searchList);

                String empIds = itemWithoutDuplicates.toString().trim();
                String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
                String strId = a1.replaceAll("\\s", "");
                Log.e("ITEM ID", "---------------------------------" + strId);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(0, prodPlanHeader.getProductionHeaderId(), sdf1.format(System.currentTimeMillis()), 1, fromId, fromName, toId, toName, loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), loginUser.getUser().getId(), sdf1.format(System.currentTimeMillis()), 4, 0, 0, postProductionPlanHeader.getItemGrp1(), 1, "", strId, prodPlanHeader.getIsPlanned(), type, 0, sdf.format(System.currentTimeMillis()), 0, sdf.format(System.currentTimeMillis()), billDetailList);
                saveDetail(billOfMaterialHeader);
            }else {
                Toast.makeText(getActivity(), "Please search item........", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader) {
        Log.e("PARAMETER","---------------------------------------PRODUCTION ISSUES HEADER--------------------------"+billOfMaterialHeader);

        if (Constants.isOnline(getActivity())) {
            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " ------------------------------SAVE ISSUES HEADER------------------------ " + response.body());
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new MainFragment(), "MainFragment");
                            ft.commit();
                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
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
                        commonDialog.dismiss();
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
                    commonDialog.dismiss();
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

    private void getSettingKey(String dept, final String itemId) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+dept);
        if (Constants.isOnline(getActivity())) {
            commonDialog1 = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog1.show();

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
                                getSfDetailsForIssue(prodPlanHeader.getProductionHeaderId(), configure.getFrItemStockConfigure().get(i).getSettingValue(),itemId);
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

                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
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
                public void onFailure(Call<Configure> call, Throwable t) {
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

    private void getSfDetailsForIssue(Integer productionHeaderId, Integer deptId, String itemId) {
        Log.e("PARAMETER","        PROD ID   "+productionHeaderId+"       DEPT ID       "+deptId+"         ITEM ID      "+itemId);
        if (Constants.isOnline(getActivity())) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<DeptDetail> listCall = Constants.myInterface.getSfDetailsForIssue(productionHeaderId,deptId,itemId);
            listCall.enqueue(new Callback<DeptDetail>() {
                @Override
                public void onResponse(Call<DeptDetail> call, Response<DeptDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION : ", " ----------------------ISSUES SEARCH----------------------- " + response.body().getSfPlanDetailForMixing());
                            searchList.clear();

                            if(response.body().getSfPlanDetailForMixing()!=null) {
                                for (int j = 0; j < response.body().getSfPlanDetailForMixing().size(); j++) {
                                    searchList.add(response.body().getSfPlanDetailForMixing().get(j));
                                }
                            }
                            Log.e("PRODUCTION : ", " ----------------------ISSUES SEARCH LIST----------------------- " + searchList);
                            for(int i=0;i<searchList.size();i++)
                            {
                                searchList.get(i).setEditTotal(searchList.get(i).getTotal()/1000);
                            }
                            Log.e("PRODUCTION : ", " ----------------------ISSUES SEARCH LIST----------------------- " + searchList);
                            IssuesAdapter  adapter = new IssuesAdapter(searchList, getContext());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
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
                public void onFailure(Call<DeptDetail> call, Throwable t) {
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

    private void showDialog() {
        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.custom_dilaog_fullscreen_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);

        groupAdapter = new GrouptListDialogAdapter(groupList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(groupAdapter);

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
                    if (groupAdapter != null) {
                        filterDept(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterDept(String text) {
        ArrayList<Group> temp = new ArrayList();
        for (Group d : groupList) {
            if (d.getMiniCatName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        groupAdapter.updateList(temp);
    }

    public class GrouptListDialogAdapter extends RecyclerView.Adapter<GrouptListDialogAdapter.MyViewHolder> {

        private ArrayList<Group> groupList;
        private Context context;

        public GrouptListDialogAdapter(ArrayList<Group> groupList, Context context) {
            this.groupList = groupList;
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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_group_list, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final Group model = groupList.get(i);

            myViewHolder.tvName.setText(model.getMiniCatName());

            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    getSfDetailsForIssue(0,0,"");
                    tvGroup.setText(""+model.getMiniCatName());
                    tvGroupId.setText(""+model.getMiniCatId());
                    groupId= Integer.parseInt(tvGroupId.getText().toString());
                   getProdSetting("PROD",groupId);
                }
            });
        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        public void updateList(ArrayList<Group> list) {
            groupList = list;
            notifyDataSetChanged();
        }

    }

    private void getProdSetting(String deptId, final int groupId) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+deptId  +"               GROUP ID           "+groupId);
        if (Constants.isOnline(getActivity())) {
            commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(deptId);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE PROD: ", " ---------------PROD DEPT------------------- " + response.body());
                            // frItemStockConfiguresList.clear();
                            Configure configure=response.body();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getBmsSetting(groupId,frItemStockConfiguresList.get(i).getSettingValue(),prodPlanHeader);
                            }

                            //  commonDialog.dismiss();

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

    private void getBmsSetting(final int groupId, final Integer prodId, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","                 SETTINGPROD     "+prodId  +"               GROUP ID           "+groupId+"               HEADER           "+prodPlanHeader);
        if (Constants.isOnline(getActivity())) {
//            commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
//            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue("BMS");
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE MIX: ", " ---------------MIXING DEPT------------------- " + response.body());
                          //  frItemStockConfiguresList.clear();
                            Configure configure=response.body();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getItemSetting(groupId, prodPlanHeader.getProductionHeaderId(),prodId,frItemStockConfiguresList.get(i).getSettingValue());
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

    private void getItemSetting(int groupId, Integer productionHeaderId, Integer prodId, Integer mixId) {
        Log.e("PARAMETER","         GROUP ID     "+groupId+"            PROD HEADER    "+productionHeaderId+"       FROM DEPT     "+prodId+"       TO DEPT    "+mixId);
        if (Constants.isOnline(getContext())) {
//            final CommonDialog commonDialog = new CommonDialog(getActivity(), "Loading", "Please Wait...");
//            commonDialog.show();

            Call<ArrayList<Item>> listCall = Constants.myInterface.findItemsByGrpIdForRmIssue(groupId,productionHeaderId,prodId,mixId);
            listCall.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Item LIST : ", " - " + response.body());
                            itemList.clear();
                            itemList=response.body();

                            assignStaticItemList.clear();
                            assignStaticItemList = itemList;

                            for (int i = 0; i < assignStaticItemList.size(); i++) {
                                assignStaticItemList.get(i).setChecked(false);
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
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
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
        View v = li.inflate(R.layout.custom_dialog_button_with_search, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        RecyclerView rvCustomerList = dialog.findViewById(R.id.rvCustomerList);
        EditText edSearch = dialog.findViewById(R.id.edSearch);
        Button btnSubmit=dialog.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ArrayList<Item> assignedArray = new ArrayList<>();
                final ArrayList<Integer> assignedItemIdArray = new ArrayList<>();
                final ArrayList<String> assignedItemNameArray = new ArrayList<>();

                if (assignStaticItemList != null) {
                    if (assignStaticItemList.size() > 0) {
                        assignedArray.clear();
                        for (int i = 0; i < assignStaticItemList.size(); i++) {
                            if (assignStaticItemList.get(i).getChecked()) {
                                assignedArray.add(assignStaticItemList.get(i));
                                assignedItemIdArray.add(assignStaticItemList.get(i).getId());
                                assignedItemNameArray.add(assignStaticItemList.get(i).getItemName());

                            }
                        }
                    }
                    Log.e("ASSIGN ITEM", "---------------------------------" + assignedArray);
                    Log.e("ASSIGN ITEM ID", "---------------------------------" + assignedItemIdArray);
                    Log.e("ASSIGN ITEM Name", "---------------------------------" + assignedItemNameArray);

                    String empIds = assignedItemIdArray.toString().trim();
                    Log.e("ASSIGN EMP ID", "---------------------------------" + empIds);

                    String a1 = "" + empIds.substring(1, empIds.length() - 1).replace("][", ",") + "";
                    stringId = a1.replaceAll("\\s", "");

                    Log.e("ASSIGN EMP ID STRING", "---------------------------------" + stringId);
                    Log.e("ASSIGN EMP ID STRING1", "---------------------------------" + a1);

                    String empName = assignedItemNameArray.toString().trim();
                    Log.e("ASSIGN EMP NAME", "---------------------------------" + empName);

                    stringName = "" + empName.substring(1, empName.length() - 1).replace("][", ",") + "";

                    Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringName);


                    tvItem.setText("" + stringName);
                    tvItemId.setText("" + stringId);
                }
            }
        });


        Log.e("ITEM LIST", "---------------------------------" + itemList);
        itemAdapter = new ItemListDialogAdapter(itemList, getContext(),type);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvCustomerList.setLayoutManager(mLayoutManager);
        rvCustomerList.setItemAnimator(new DefaultItemAnimator());
        rvCustomerList.setAdapter(itemAdapter);

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
                    if (itemAdapter != null) {
                        filterEmp(editable.toString());
                    }
                } catch (Exception e) {
                }
            }
        });

        dialog.show();
    }

    void filterEmp(String text) {
        ArrayList<Item> temp = new ArrayList();
        for (Item d : itemList) {
            if (d.getItemName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        itemAdapter.updateList(temp);
    }


    public class ItemListDialogAdapter extends RecyclerView.Adapter<ItemListDialogAdapter.MyViewHolder> {

        private ArrayList<Item> itemList;
        private Context context;
        int type;


        public ItemListDialogAdapter(ArrayList<Item> itemList, Context context,int type) {
            this.itemList = itemList;
            this.context = context;
            this.type = type;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvAddress;
            public CheckBox checkBox;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvName);
                tvAddress = view.findViewById(R.id.tvAddress);
                checkBox = view.findViewById(R.id.checkBox);
                linearLayout = view.findViewById(R.id.linearLayout);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_item_dialog, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
            final Item model = itemList.get(i);
            myViewHolder.tvName.setText(model.getItemName());
            Log.e("Type","---------------------------------"+type);
            if(type==0)
            {
                if(model.getItemId().equalsIgnoreCase("1"))
                {
                    Log.e("Item id","---------------------------------"+model.getItemId());
                    myViewHolder.checkBox.setEnabled(false);
                }else if(model.getItemId().equalsIgnoreCase("0"))
                {
                    Log.e("Item id","---------------------------------"+model.getItemId());
                    myViewHolder.checkBox.setEnabled(true);
                }
            }else if(type==1)
            {
                myViewHolder.checkBox.setEnabled(true);
            }

            myViewHolder.checkBox.setChecked(model.getChecked());

            myViewHolder.checkBox.setTag(model);

            myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Item item = (Item) cb.getTag();

                    item.setChecked(cb.isChecked());
                    Log.e("OnClick","----------------------------------ADAPTER---------------------------------"+itemList);
                    model.setChecked(cb.isChecked());

                }
            });




//            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    tvItem.setText(""+model.getItemName());
//                    tvItemId.setText(""+model.getItemId());
//
//                }
//            });
        }



        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public void updateList(ArrayList<Item> list) {
            itemList = list;
            notifyDataSetChanged();
        }

    }

}
