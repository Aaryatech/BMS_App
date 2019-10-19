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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.Production;
import com.example.bms_app.model.Type;
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
public class ManualProdFragment extends Fragment implements View.OnClickListener{

public EditText edQtyKg;
private TextView tvProdNo,tvDate;
ProdPlanHeader prodPlanHeader;
Button btnSubmit,btnSerach;
RecyclerView recyclerView;
TextView tvTypeId,tvType,tvProduct,tvProductId;

Dialog dialog;
int typeId;
CommonDialog commonDialog;
private List<FrItemStockConfigure> frItemStockConfiguresList;
TypeListDialogAdapter typeAdapter;
ProductionListDialogAdapter productionAdapter;
List<Type> typeList=new ArrayList<>();
List<Production> productionList=new ArrayList<>();

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

        getTypeList(0);

        tvType.setOnClickListener(this);
        tvProduct.setOnClickListener(this);
        btnSerach.setOnClickListener(this);

        return view;
    }

    private void getTypeList(int delStatus) {
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
                            typeList.clear();
                            typeList=response.body();

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

            }
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
