package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfAllMaterialHeader;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.utils.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueFromBMSAdapter extends RecyclerView.Adapter<IssueFromBMSAdapter.MyViewHolder> {
    private List<BillOfAllMaterialHeader> reqAllList;
    private Context context;
    private List<BillOfMaterialDetailed> detailList = new ArrayList<>();
    BillOfMaterialHeader billOfMaterialHeader;
    private IssueItemAdapter mAdapter;

    public IssueFromBMSAdapter(List<BillOfAllMaterialHeader> reqAllList, Context context) {
        this.reqAllList = reqAllList;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueFromBMSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_all_bms_issue, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final IssueFromBMSAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfAllMaterialHeader model=reqAllList.get(i);

        Log.e("ADAPTER LIST","-------------------------------------------------------"+reqAllList);

        myViewHolder.tvProductId.setText("Prod Id : "+model.getProductionId());
        myViewHolder.tvDate.setText(""+model.getReqDate());
        myViewHolder.tvDept.setText(""+model.getToDeptName());

        if(model.getStatus()==0)
        {
            myViewHolder.tvStatus.setText("Pending");
        }else if(model.getStatus()==1)
        {
            myViewHolder.tvStatus.setText("Approved");
        }if(model.getStatus()==2)
        {
            myViewHolder.tvStatus.setText("Rejected");
        }if(model.getStatus()==3)
        {
            myViewHolder.tvStatus.setText("Approved Rejected");
        }if(model.getStatus()==4)
        {
            myViewHolder.tvStatus.setText("Request Close");
        }


        if (model.getVisibleStatus() == 1) {
            myViewHolder.llItems.setVisibility(View.VISIBLE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
        } else {
            myViewHolder.llItems.setVisibility(View.GONE);
            myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));
        }

        myViewHolder.tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getVisibleStatus() == 0) {
                    model.setVisibleStatus(1);
                    myViewHolder.llItems.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_up));
                    getAllProductionDetail(model.getReqId(),model,myViewHolder);
                } else if (model.getVisibleStatus() == 1) {
                    model.setVisibleStatus(0);
                    myViewHolder.llItems.setVisibility(View.GONE);
                    myViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down));

                }

            }
        });




//        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getAllProductionDetail(model.getReqId(),model);
//                //  new DeptDialog(context,model,detailList).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return reqAllList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvDept,tvItems;
        public CardView cardView;
        public RecyclerView recyclerView;
        public ImageView imageView,ivEdit;
        public LinearLayout llItems;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDept=itemView.findViewById(R.id.tvDept);
            cardView=itemView.findViewById(R.id.cardView);

            tvDate = itemView.findViewById(R.id.tvDate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            tvItems = itemView.findViewById(R.id.tvItems);
            llItems = itemView.findViewById(R.id.llItems);
            imageView = itemView.findViewById(R.id.imageView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }

    private void getAllProductionDetail(Integer reqId, final BillOfAllMaterialHeader model, final MyViewHolder myViewHolder) {
        Log.e("PARAMETER","                 REQ ID     "+reqId );
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<BillOfMaterialHeader> listCall = Constants.myInterface.getDetailedwithreqId(reqId);
            listCall.enqueue(new Callback<BillOfMaterialHeader>() {
                @Override
                public void onResponse(Call<BillOfMaterialHeader> call, Response<BillOfMaterialHeader> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION DETAIL: ", " - " + response.body());
                            // detailList.clear();
                            // detailList=response.body();
                            billOfMaterialHeader=response.body();
                            detailList.clear();
                            detailList=response.body().getBillOfMaterialDetailed();

                            mAdapter = new IssueItemAdapter(detailList, context);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            myViewHolder.recyclerView.setLayoutManager(mLayoutManager);
                            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                            myViewHolder.recyclerView.setAdapter(mAdapter);

                          // new DeptDialog(context,model,detailList).show();

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
                public void onFailure(Call<BillOfMaterialHeader> call, Throwable t) {
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

    private class DeptDialog extends Dialog {
        public Button btnCancel;
        public RecyclerView recyclerView;
        public ImageView ivClose;
        private IssueItemAdapter mAdapter;
        public TextView tvDate,tvNo,tvDept;
        private List<BillOfMaterialDetailed> detailList = new ArrayList<>();
        //ProdPlanHeader prodDetail;
        // int productionHeaderId;
        BillOfAllMaterialHeader billOfAllMaterialHeader;



        public DeptDialog(Context context,BillOfAllMaterialHeader billOfAllMaterialHeader, List<BillOfMaterialDetailed> detailList) {
            super(context);
            this.billOfAllMaterialHeader=billOfAllMaterialHeader;
            this.detailList=detailList;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_layout_issue_bms);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);


            btnCancel = (Button) findViewById(R.id.btnCancel);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            tvDate = (TextView) findViewById(R.id.tvDate);
            tvNo = (TextView) findViewById(R.id.tvNo);
            tvDept = (TextView) findViewById(R.id.tvDept);

            try{
                tvDate.setText("Prod Date : "+billOfAllMaterialHeader.getProductionDate());
                tvNo.setText("Prod No : "+billOfAllMaterialHeader.getProductionId());
                tvDept.setText("Dept : "+"PROD-BMS");
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });



            Log.e("Detail List Dialog","----------------------------------------"+detailList);
            mAdapter = new IssueItemAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            // }

        }

    }
}
