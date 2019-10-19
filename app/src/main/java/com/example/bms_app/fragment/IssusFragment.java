package com.example.bms_app.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.IssuesAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Group;
import com.example.bms_app.model.Item;
import com.example.bms_app.model.ProdPlanHeader;
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
public class IssusFragment extends Fragment implements View.OnClickListener{
    private TextView tvProdNo,tvDate;
    ProdPlanHeader prodPlanHeader;
    Button btnSubmit,btnSerach;
    TextView tvItemId,tvItem,tvGroup,tvGroupId;
    RecyclerView recyclerView;
    ArrayList<Group> groupList = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    List<SfPlanDetailForMixing> searchList = new ArrayList<>();

    Dialog dialog;
    int groupId;
    CommonDialog commonDialog,commonDialog1;
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    GrouptListDialogAdapter groupAdapter;
    ItemListDialogAdapter itemAdapter;
    String stringId,stringName;
    public static ArrayList<Item> assignStaticItemList = new ArrayList<>();

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

        getGroup();

        tvGroup.setOnClickListener(this);
        tvItem.setOnClickListener(this);
        btnSerach.setOnClickListener(this);

        return view;
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSerach)
        {
           // getSfDetailsForIssue
            String strGroup,strItem,srtItemId;
            boolean isValidGroup=false,isValidItem=false;

            strGroup=tvGroup.getText().toString();
            strItem=tvItem.getText().toString();
            srtItemId=tvItemId.getText().toString();

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

            if(isValidGroup && isValidItem)
            {
                getSettingKey("BMS",stringId);

            }

        }else if(v.getId()==R.id.tvGroup)
        {
            showDialog();
        }else if(v.getId()==R.id.tvItem)
        {
            showDialog1();
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

                            Log.e("PRODUCTION : ", " ----------------------ISSUES SEARCH----------------------- " + response.body());
                            searchList.clear();
                            searchList=response.body().getSfPlanDetailForMixing();
                            for(int i=0;i<searchList.size();i++)
                            {
                                searchList.get(i).setEditTotal(searchList.get(i).getTotal());
                            }

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
            //holder.tvAddress.setText(model.getCustAddress());

            myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
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
                            if (assignStaticItemList.get(i).isChecked()) {
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

                    // stringName = a.replaceAll("\\s","");

                    Log.e("ASSIGN EMP NAME STRING", "---------------------------------" + stringName);
                    //Log.e("ASSIGN EMP NAME STRING1","---------------------------------"+a);

                    tvItem.setText("" + stringName);
                    tvItemId.setText("" + stringId);
                }
            }
        });

        itemAdapter = new ItemListDialogAdapter(itemList, getContext());
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
        //public ArrayList<String> assignedItemIdArray;

        public ItemListDialogAdapter(ArrayList<Item> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
            //this.assignedItemIdArray = assignedItemIdArray;
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
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
            final Item model = itemList.get(i);
            final int pos = i;
            myViewHolder.tvName.setText(model.getItemName());

            myViewHolder.checkBox.setChecked(itemList.get(i).isChecked());

            myViewHolder.checkBox.setTag(itemList.get(i));

            myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Item employee = (Item) cb.getTag();

                    employee.setChecked(cb.isChecked());
                    itemList.get(pos).setChecked(cb.isChecked());

                }
            });

//            if(assignedItemIdArray!=null)
//            {
//                for(int j=0;j<assignedItemIdArray.size();j++)
//                {
//                    if(model.getItemId().equalsIgnoreCase(assignedItemIdArray.get(j)))
//                    {
//                        myViewHolder.checkBox.setEnabled(false);
//                    }
//                }
//            }


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
