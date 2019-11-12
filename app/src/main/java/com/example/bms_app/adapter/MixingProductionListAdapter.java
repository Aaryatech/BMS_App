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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.BuildConfig;
import com.example.bms_app.R;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.model.MixingDetailList;
import com.example.bms_app.model.MixingDetailed;
import com.example.bms_app.model.MixingHeaderList;
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

public class MixingProductionListAdapter extends RecyclerView.Adapter<MixingProductionListAdapter.MyViewHolder> {

    private List<MixingHeaderList> mixingList;
    private Context context;
    private List<MixingDetailed> detailList = new ArrayList<>();

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

    public MixingProductionListAdapter(List<MixingHeaderList> mixingList, Context context) {
        this.mixingList = mixingList;
        this.context = context;

        dir = new File(Environment.getExternalStorageDirectory() + File.separator, "BMS" + File.separator + "Report");
        if (!dir.exists()) {
            dir.mkdirs();
        }

    }

    @NonNull
    @Override
    public MixingProductionListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_mixing_production_list_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MixingProductionListAdapter.MyViewHolder myViewHolder, int i) {
        final MixingHeaderList model=mixingList.get(i);
        myViewHolder.tvProductId.setText("Prod Id : "+model.getProductionId());
        myViewHolder.tvDate.setText(""+model.getMixDate());


        if(model.getExInt1()==10)
        {
            myViewHolder.tvDept.setText("MIX");
        }else if(model.getExInt1()==11)
        {
            myViewHolder.tvDept.setText("BMS");
        }

        if(model.getStatus()==1) {
            myViewHolder.tvStatus.setText("Add From Plan");
        }else if(model.getStatus()==2)
        {
            myViewHolder.tvStatus.setText("Add From Production");
        }else if(model.getStatus()==3)
        {
            myViewHolder.tvStatus.setText("Start Production");
        }else if(model.getStatus()==4)
        {
            myViewHolder.tvStatus.setText("Production Completed");
        }else if(model.getStatus()==5)
        {
            myViewHolder.tvStatus.setText("Closed");
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMixingDetail(model.getMixId(),model);
               // new DeptDialog(context,model).show();
            }
        });
    }

    private void getMixingDetail(Integer mixId, final MixingHeaderList model) {
        Log.e("PARAMETER","                 MIX ID     "+mixId  +"               MIXING HEADER LIST           "+model);
        if (Constants.isOnline(context)) {
            final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
            commonDialog.show();

            Call<MixingDetailList> listCall = Constants.myInterface.getDetailedwithMixId(mixId);
            listCall.enqueue(new Callback<MixingDetailList>() {
                @Override
                public void onResponse(Call<MixingDetailList> call, Response<MixingDetailList> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("PRODUCTION DETAIL: ", " - " + response.body());
                            detailList.clear();
                            detailList=response.body().getMixingDetailed();
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
                public void onFailure(Call<MixingDetailList> call, Throwable t) {
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

    @Override
    public int getItemCount() {
        return mixingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductId,tvDate,tvStatus,tvDept;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductId=itemView.findViewById(R.id.tvProductId);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvDept=itemView.findViewById(R.id.tvDept);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    private class DeptDialog extends Dialog {
        public Button btnPdf,btnCancel;
        public ImageView ivClose;
        public TextView tvDate,tvNo,tvDept;
        public RecyclerView recyclerView;
        private ProductionMixingDetailListAdapter mAdapter;
        MixingHeaderList mixingHeaderList;
        String dept;

        public DeptDialog(Context context, MixingHeaderList mixingHeaderList) {
            super(context);
            this.mixingHeaderList=mixingHeaderList;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.dialog_production_mixing_list);
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
            ivClose =(ImageView) findViewById(R.id.ivClose);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            tvDate = (TextView) findViewById(R.id.tvDate);
            tvNo = (TextView) findViewById(R.id.tvNo);
            tvDept = (TextView) findViewById(R.id.tvDept);

            try{
                tvDate.setText("Prod Date : "+mixingHeaderList.getMixDate());
                tvNo.setText("Prod No : "+mixingHeaderList.getProductionId());
                tvDept.setText("Dept : "+"PROD-MIX");
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            btnPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // dismiss();
                    createMixingPDF(detailList,mixingHeaderList);

                }
            });


            mAdapter = new ProductionMixingDetailListAdapter(detailList,context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        }

    }

    private void createMixingPDF(List<MixingDetailed> detailList, MixingHeaderList mixingHeaderList) {
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

            String fileName = "Production_Mixing_Report_" + mixingHeaderList.getMixId() + ".pdf";
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


                PdfPTable ptDate = new PdfPTable(4);
                float[] colWidth = new float[]{17, 40, 50, 25};
                ptDate.setWidths(colWidth);
                ptDate.setTotalWidth(colWidth);
                ptDate.setWidthPercentage(100);

                cell = new PdfPCell(new Paragraph("Prod No: ", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                //cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("" +mixingHeaderList.getProductionId()));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(0);
                cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("Date: ", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + mixingHeaderList.getMixDate(), boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(2);
                cell.setColspan(1);
                ptDate.addCell(cell);


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

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptDate);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(4);
                float[] columnWidth = new float[]{10, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(7);
                cell.addElement(pTable);

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


                cell = new PdfPCell(new Phrase("SF Name", boldTextFont));
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

                    table.addCell("" + detailList.get(i).getSfName());

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getReceivedQty()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + detailList.get(i).getProductionQty()));
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
