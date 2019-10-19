package com.example.bms_app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.BuildConfig;
import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.BillOfAllMaterialHeader;
import com.example.bms_app.model.BillOfMaterialDetailed;
import com.example.bms_app.model.BillOfMaterialHeader;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForAllAdapter extends RecyclerView.Adapter<RequestForAllAdapter.MyViewHolder> {

    private List<BillOfAllMaterialHeader> reqAllList;
    private List<BillOfMaterialDetailed> detailList = new ArrayList<>();
    BillOfMaterialHeader billOfMaterialHeader;
    private Context context;

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

    public RequestForAllAdapter(List<BillOfAllMaterialHeader> reqAllList, Context context) {
        this.reqAllList = reqAllList;
        this.context = context;

        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "BMS" + File.separator + "Report");
        if (!dir.exists()) {
            dir.mkdirs();
        }

//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BMS/Report";
//        dir = new File(path);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
    }

    @NonNull
    @Override
    public RequestForAllAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_all_request, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestForAllAdapter.MyViewHolder myViewHolder, int i) {
        final BillOfAllMaterialHeader model=reqAllList.get(i);

        Log.e("ADAPTER LIST","-------------------------------------------------------"+reqAllList);

        myViewHolder.tvProductId.setText(""+model.getProductionId());
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
        public Button btnCancel,btnPdf;
        public RecyclerView recyclerView;
        private AllProductionDetailAdapter mAdapter;
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
            setContentView(R.layout.dialog_layout_all_production);
            setCancelable(false);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER | Gravity.RIGHT;
            wlp.x = 10;
            wlp.y = 10;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            btnPdf = (Button) findViewById(R.id.btnPdf);
            btnCancel = (Button) findViewById(R.id.btnCancel);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            btnPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAllRequestPDF(detailList,billOfAllMaterialHeader);
                }
            });

//            if (billOfMaterialHeader.getBillOfMaterialDetailed() != null) {
//                billOfMaterialHeader.getBillOfMaterialDetailed().clear();
//                for (int i = 0; i < billOfMaterialHeader.getBillOfMaterialDetailed().size(); i++) {
//                    detailList.add(billOfMaterialHeader.getBillOfMaterialDetailed().get(i));
//                }
                Log.e("Detail List Dialog","----------------------------------------"+detailList);
                mAdapter = new AllProductionDetailAdapter(detailList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
           // }

        }



    }

    private void createAllRequestPDF(List<BillOfMaterialDetailed> detailList,BillOfAllMaterialHeader billOfAllMaterialHeader) {
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

            String fileName = "Show_all_Request_Report_" + billOfAllMaterialHeader.getProductionId() + ".pdf";
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

                cell = new PdfPCell(new Paragraph("Report-Request to BMS", boldFont));
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

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{10, 30, 30, 30, 30,30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("Sr.No.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("Item Name", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("Requested Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Single Cut", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Double Cut", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Issue Qty", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                float total = 0;
                for (int i = 0; i < detailList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + detailList.get(i).getRmName());


                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getRmReqQty()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getExVarchar1()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getExVarchar2()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);


                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getRmIssueQty()));
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
