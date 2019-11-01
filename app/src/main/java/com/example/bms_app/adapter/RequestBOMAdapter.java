package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.activity.MainActivity;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.fragment.ShowRequestBOMFragment;
import com.example.bms_app.model.BillOfAllMaterialHeader;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.Info;
import com.example.bms_app.utils.CommonDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestBOMAdapter extends RecyclerView.Adapter<RequestBOMAdapter.MyViewHolder> {

    private List<BillOfAllMaterialHeader> reqAllList;
    private List<BillOfMaterialDetailed> detailList = new ArrayList<>();
    BillOfMaterialHeader billOfMaterialHeader;
    private Context context;

    public RequestBOMAdapter(List<BillOfAllMaterialHeader> reqAllList, Context context) {
        this.reqAllList = reqAllList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestBOMAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_all_request, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestBOMAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfAllMaterialHeader model=reqAllList.get(i);

        Log.e("ADAPTER LIST","-------------------------------------------------------"+reqAllList);

        myViewHolder.tvProductId.setText(""+model.getProductionId());
        myViewHolder.tvDate.setText(""+model.getReqDate());
        myViewHolder.tvDept.setText(""+model.getFromDeptName());

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



        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllProductionDetail(model.getReqId(),model);
              //  new DeptDialog(context,model,detailList).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return reqAllList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvDept;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDept=itemView.findViewById(R.id.tvDept);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    private class DeptDialog extends Dialog {
        public Button btnCancel,btnSubmit,btnAppReject;
        public RecyclerView recyclerView;
        public ImageView ivClose;
        private AllBomRequestAdapter mAdapter;
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
            setContentView(R.layout.dialog_layout_request_for_bom);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            btnAppReject = (Button) findViewById(R.id.btnAppReject);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            if(billOfAllMaterialHeader.getStatus()==0)
            {
                btnSubmit.setVisibility(View.VISIBLE);
                btnAppReject.setVisibility(View.GONE);
            }else if(billOfAllMaterialHeader.getStatus()==2)
            {
                btnSubmit.setVisibility(View.GONE);
                btnAppReject.setVisibility(View.VISIBLE);
            }else if(billOfAllMaterialHeader.getStatus()==1 || billOfAllMaterialHeader.getStatus()==3 || billOfAllMaterialHeader.getStatus()==4)
            {
                btnSubmit.setVisibility(View.GONE);
                btnAppReject.setVisibility(View.VISIBLE);
                btnAppReject.setClickable(false);
            }


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                    BillOfMaterialHeader billOfMaterialHeader = new BillOfMaterialHeader(billOfAllMaterialHeader.getReqId(), billOfAllMaterialHeader.getProductionId(),billOfAllMaterialHeader.getProductionDate(),billOfAllMaterialHeader.getIsProduction(),billOfAllMaterialHeader.getFromDeptId(),billOfAllMaterialHeader.getFromDeptName(),billOfAllMaterialHeader.getToDeptId(),billOfAllMaterialHeader.getToDeptName(),0,billOfAllMaterialHeader.getReqDate(),billOfAllMaterialHeader.getApprovedUserId(),sdf1.format(System.currentTimeMillis()),1,billOfAllMaterialHeader.getExBool1(),billOfAllMaterialHeader.getDelStatus(),billOfAllMaterialHeader.getExInt1(),billOfAllMaterialHeader.getExInt2(),"","",billOfAllMaterialHeader.getIsPlan(),billOfAllMaterialHeader.getIsManual(),billOfAllMaterialHeader.getRejUserId(),billOfAllMaterialHeader.getRejDate(),billOfAllMaterialHeader.getRejApproveUserId(),billOfAllMaterialHeader.getRejApproveDate(),detailList);
                    saveDetail(billOfMaterialHeader);

                }
            });

//            if (billOfMaterialHeader.getBillOfMaterialDetailed() != null) {
//                billOfMaterialHeader.getBillOfMaterialDetailed().clear();
//                for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {
//                    detailList.add(billOfMaterialHeader.getBillOfMaterialDetailed().get(i));
//                }
            Log.e("Detail List Dialog","----------------------------------------"+detailList);
            mAdapter = new AllBomRequestAdapter(detailList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            // }

        }

    }

    private void saveDetail(BillOfMaterialHeader billOfMaterialHeader) {
        Log.e("PARAMETER","---------------------------------------REQUEST BOM HEADER--------------------------"+billOfMaterialHeader);

        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> listCall = Constants.myInterface.saveBom(billOfMaterialHeader);
            listCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("HEADER : ", " ------------------------------SAVE REQUEST BOM------------------------ " + response.body());
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            MainActivity activity=(MainActivity)context;
                            FragmentTransaction ft =activity.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, new ShowRequestBOMFragment(), "MainFragment");
                            ft.commit();

                            commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("" + context.getResources().getString(R.string.app_name));
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                        builder.setTitle("" + context.getResources().getString(R.string.app_name));
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("" + context.getResources().getString(R.string.app_name));
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
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllProductionDetail(Integer reqId, final BillOfAllMaterialHeader model) {
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
                            detailList=response.body().getBillOfMaterialDetailed();

                            new DeptDialog(context,model,detailList).show();

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

}
