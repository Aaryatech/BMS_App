package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

import com.example.bms_app.BuildConfig;
import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.GetTempMixItemDetailList;
import com.example.bms_app.model.MixingDetail;
import com.example.bms_app.model.MixingDetailedSave;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.ProdMixingReqP1;
import com.example.bms_app.model.ProdPlanHeader;
import com.example.bms_app.model.TempMixItemDetail;
import com.example.bms_app.model.TempMixing;
import com.example.bms_app.utils.CommonDialog;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionMixingAdapter extends RecyclerView.Adapter<ProductionMixingAdapter.MyViewHolder> {

    private List<ProdPlanHeader> reqMixingList;
    private List<ProdMixingReqP1> detailList = new ArrayList<>();
    private List<TempMixItemDetail> mixDetailList = new ArrayList<>();
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    ArrayList<TempMixing> tempMixingDetailList = new ArrayList<>();
    ArrayList<MixingDetailedSave> mixList = new ArrayList<>();
    private Context context;
     CommonDialog commonDialog,commonDialog1;


    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");

    public ProductionMixingAdapter(List<ProdPlanHeader> reqMixingList, Context context) {
        this.reqMixingList = reqMixingList;
        this.context = context;

        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "BMS" + File.separator + "Report");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @NonNull
    @Override
    public ProductionMixingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.prod_mixing_header_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionMixingAdapter.MyViewHolder myViewHolder, int i) {
        final ProdPlanHeader model=reqMixingList.get(i);
        myViewHolder.tvProductId.setText(""+model.getProductionHeaderId());
        myViewHolder.tvDate.setText(model.getProductionDate());

        if(model.getIsPlanned()==1) {
            myViewHolder.tvIsPlane.setText("Yes");
        }else if(model.getIsPlanned()==0)
        {
            myViewHolder.tvIsPlane.setText("No");
        }

        if(model.getProductionStatus().equalsIgnoreCase("1")) {
            myViewHolder.tvStatus.setText("Add From Plan");
        }else if(model.getProductionStatus().equalsIgnoreCase("2"))
        {
            myViewHolder.tvStatus.setText("Add From Production");
        }else if(model.getProductionStatus().equalsIgnoreCase("3"))
        {
            myViewHolder.tvStatus.setText("Start Production");
        }else if(model.getProductionStatus().equalsIgnoreCase("4"))
        {
            myViewHolder.tvStatus.setText("Complet Production");
        }else if(model.getProductionStatus().equalsIgnoreCase("5"))
        {
            myViewHolder.tvStatus.setText("Closed");
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductionSetting("MIX",model.getProductionHeaderId(),model);
               // new DeptDialog(context,model).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return reqMixingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvStatus,tvDate,tvIsPlane;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvIsPlane=itemView.findViewById(R.id.tvIsPlane);
            cardView=itemView.findViewById(R.id.cardView);

        }
    }

    private void getProductionSetting(final String settingKey, final int productionHeaderId, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+settingKey  +"               HEADER           "+productionHeaderId);
        if (Constants.isOnline(context)) {
             commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Configure> listCall = Constants.myInterface.getDeptSettingValue(settingKey);
            listCall.enqueue(new Callback<Configure>() {
                @Override
                public void onResponse(Call<Configure> call, Response<Configure> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SETTING RESPONCE MIX: ", " ---------------MIXING DEPT------------------- " + response.body());
                            // frItemStockConfiguresList.clear();
                            Configure configure=response.body();
                            frItemStockConfiguresList=response.body().getFrItemStockConfigure();
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getMixingDetail(productionHeaderId, configure.getFrItemStockConfigure().get(i).getSettingValue(),prodPlanHeader);
                            }


                          //  commonDialog.dismiss();

                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
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

    private void getMixingDetail(int productionHeaderId, Integer deptId, final ProdPlanHeader model) {
        Log.e("PARAMETER","                 HEADER ID     "+productionHeaderId  +"               SETTING VALUES           "+deptId);
        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<MixingDetail> listCall = Constants.myInterface.getSfPlanDetailForMixing(productionHeaderId,deptId);
            listCall.enqueue(new Callback<MixingDetail>() {
                @Override
                public void onResponse(Call<MixingDetail> call, Response<MixingDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION DETAIL: ", " - " + response.body());
                            detailList.clear();
                            detailList=response.body().getProdMixingReqP1();
                            new DeptDialog(context,model).show();
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
                public void onFailure(Call<MixingDetail> call, Throwable t) {
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
        public Button btnPdf,btnAddProd;
        public ImageView ivClose;
        public RecyclerView recyclerView;
        private ProductionMixingDetailAdapter mAdapter;
        ProdPlanHeader prodPlanHeader;
        String dept;

        public DeptDialog(Context context, ProdPlanHeader prodPlanHeader) {
            super(context);
            this.prodPlanHeader=prodPlanHeader;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_production_mixing);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnPdf = (Button) findViewById(R.id.btnPdf);
            btnAddProd = (Button) findViewById(R.id.btnAddProd);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // dismiss();
                    createAllRequestPDF(detailList,prodPlanHeader);

                }
            });

            btnAddProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // dismiss();
                    for(int i=0;i<detailList.size();i++) {
                        TempMixing model = new TempMixing(0, 0,detailList.get(i).getRmId(),detailList.get(i).getTotal()*detailList.get(i).getMulFactor(),prodPlanHeader.getProductionHeaderId());
                        tempMixingDetailList.add(model);
                    }

                    saveTempMixing(tempMixingDetailList,prodPlanHeader);

                }
            });


                mAdapter = new ProductionMixingDetailAdapter(detailList,context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

        }

    }

    private void saveTempMixing(ArrayList<TempMixing> tempMixing, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING TEMP--------------------------"+tempMixing);

        if (Constants.isOnline(context)) {
             commonDialog1 = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog1.show();

            Call<TempMixing> listCall = Constants.myInterface.insertTempMixing(tempMixing);
            listCall.enqueue(new Callback<TempMixing>() {
                @Override
                public void onResponse(Call<TempMixing> call, Response<TempMixing> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE : ", " ------------------------------SAVE TEMP MIXING------------------------ " + response.body());
                            getTempMixItemDetail(prodPlanHeader.getProductionHeaderId(),prodPlanHeader);

                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
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
                        commonDialog1.dismiss();
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
                public void onFailure(Call<TempMixing> call, Throwable t) {
                    commonDialog1.dismiss();
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

    private void getTempMixItemDetail(Integer productionHeaderId, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId);
        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<GetTempMixItemDetailList> listCall = Constants.myInterface.getTempMixItemDetail(productionHeaderId);
            listCall.enqueue(new Callback<GetTempMixItemDetailList>() {
                @Override
                public void onResponse(Call<GetTempMixItemDetailList> call, Response<GetTempMixItemDetailList> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("MIXING DETAIL: ", " - " + response.body());
                            mixDetailList.clear();
                            mixDetailList=response.body().getTempMixItemDetail();

                            for(int i=0;i<mixDetailList.size();i++)
                            {
                                for(int j=0;j<detailList.size();j++)
                                {
                                    if(detailList.get(j).getRmId()==mixDetailList.get(i).getRmId())
                                    {
                                       detailList.get(j).setTotal(detailList.get(j).getTotal()+mixDetailList.get(i).getTotal());
                                    }
                                }

                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            for(int i=0;i<detailList.size();i++) {
                                MixingDetailedSave mixingDetailedSave = new MixingDetailedSave(0, 0,detailList.get(i).getRmId(),detailList.get(i).getRmName(),detailList.get(i).getTotal(),detailList.get(i).getTotal(),sdf.format(System.currentTimeMillis()),0,0,0,String.valueOf(detailList.get(i).getMulFactor()),"","",0,detailList.get(i).getUom(),0,detailList.get(i).getPrevtotal(),detailList.get(i).getMulFactor()*detailList.get(i).getPrevtotal());
                                mixList.add(mixingDetailedSave);
                            }

                            MixingHeaderDetail mixingHeaderDetail=new MixingHeaderDetail(0,sdf.format(System.currentTimeMillis()),0,prodPlanHeader.getProductionBatch(),2,0,prodPlanHeader.getTimeSlot(),0,0,0,0,"","","",0,mixList);
                            saveMixing(mixingHeaderDetail,prodPlanHeader);
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
                            Log.e("Data Null : ", "-----------");
                            Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog1.dismiss();
                        Log.e("Exception Detail  : ", "-----------" + e.getMessage());
                        Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetTempMixItemDetailList> call, Throwable t) {
                    commonDialog1.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(context, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveMixing(MixingHeaderDetail mixingHeaderDetail, final ProdPlanHeader prodPlanHeader) {
        Log.e("PARAMETER","---------------------------------------MIXING SAVE--------------------------"+mixingHeaderDetail);

        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<MixingHeaderDetail> listCall = Constants.myInterface.insertMixingHeaderndDetailed(mixingHeaderDetail);
            listCall.enqueue(new Callback<MixingHeaderDetail>() {
                @Override
                public void onResponse(Call<MixingHeaderDetail> call, Response<MixingHeaderDetail> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("SAVE : ", " ------------------------------SAVE  MIXING------------------------ " + response.body());
                            for(int i=0;i<frItemStockConfiguresList.size();i++) {
                                getupdateisMixingandBom(prodPlanHeader.getProductionHeaderId(),0,frItemStockConfiguresList.get(i).getSettingValue());
                            }
                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
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
                        commonDialog1.dismiss();
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
                public void onFailure(Call<MixingHeaderDetail> call, Throwable t) {
                    commonDialog1.dismiss();
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

    private void getupdateisMixingandBom(Integer productionHeaderId, int flag, Integer dept) {
        Log.e("PARAMETER","                 PROD HEADER ID     "+productionHeaderId+"      FLAG      "+flag+"          DEPT            "+dept);
        if (Constants.isOnline(context)) {
//            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
//            commonDialog.show();

            Call<Integer> listCall = Constants.myInterface.updateisMixingandBom(productionHeaderId,flag,dept);
            listCall.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("UPDATE : ", " ------------------------------UPDATE MIXING------------------------ " + response.body());

                            commonDialog1.dismiss();

                        } else {
                            commonDialog1.dismiss();
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
                        commonDialog1.dismiss();
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
                public void onFailure(Call<Integer> call, Throwable t) {
                    commonDialog1.dismiss();
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

    private void createAllRequestPDF(List<ProdMixingReqP1> detailList, ProdPlanHeader prodPlanHeader) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();

        Log.e("PDF DETAIL","--------------------------------------------------"+detailList);

        Document doc = new Document();
        doc.setMargins(-16, -17, 40, 40);
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            String fileName = "Show_all_Request_Report_" + prodPlanHeader.getProductionHeaderId() + ".pdf";
            file = new File(dir, fileName);

            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            try {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Galdhar Foods", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Factory Add: Plot No.48,Chikalthana Midc, Aurangabad.Phone:0240-2466217, Email:\n" +
                        "aurangabad@monginis.net", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("Report-Mixing Request", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);


                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(7);
                float[] columnWidth = new float[]{10, 30, 30, 30, 30,30,30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(7);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("Sr.No.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("Sf Name", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("Original Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Multipl Factor", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Auto Order Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Received Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Production Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                float total = 0;
                for (int i = 0; i < detailList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + detailList.get(i).getRmName());

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getTotal()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getMulFactor()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    float qty=detailList.get(i).getTotal()*detailList.get(i).getMulFactor();

                    cell = new PdfPCell(new Phrase("" +qty));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                    cell = new PdfPCell(new Phrase("" + qty));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" ));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                }

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 50, 50};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

//                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
//                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
//                cell.setBackgroundColor(myColor);
//                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

//                Paragraph paragraph=new Paragraph("Vital1");
//                paragraph.setAlignment(c);
//                doc.add(new Paragraph("Vital1"));

                //doc.add(pt);
                doc.add(table);
                //doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
                Log.e("Mytag2","--------------------");
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, fileName);

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(context, authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
                    Log.e("Mytag1","--------------------");
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Log.e("Mytag","--------------------"+e);
            Toast.makeText(context, "Unable To Generate", Toast.LENGTH_SHORT).show();
        }

    }
}
