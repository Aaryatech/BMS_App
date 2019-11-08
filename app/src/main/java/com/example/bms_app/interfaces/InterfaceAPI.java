package com.example.bms_app.interfaces;

import com.example.bms_app.model.BillOfMaterialHeader;
import com.example.bms_app.model.BmsStockHeader;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.DeptDetail;
import com.example.bms_app.model.GetTempMixItemDetailList;
import com.example.bms_app.model.Group;
import com.example.bms_app.model.Info;
import com.example.bms_app.model.Item;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.MixingDetail;
import com.example.bms_app.model.MixingDetailList;
import com.example.bms_app.model.MixingHeader;
import com.example.bms_app.model.MixingHeaderDetail;
import com.example.bms_app.model.PostProductionPlanHeader;
import com.example.bms_app.model.Production;
import com.example.bms_app.model.ProductionDetail;
import com.example.bms_app.model.SfItemHeader;
import com.example.bms_app.model.ShowAllReq;
import com.example.bms_app.model.Stock;
import com.example.bms_app.model.StockDetail;
import com.example.bms_app.model.StockDetailSf;
import com.example.bms_app.model.TempMixing;
import com.example.bms_app.model.Type;
import com.example.bms_app.model.UpdateBmsSfStockList;
import com.example.bms_app.model.UpdateBmsStockList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceAPI {

    @POST("getProdPlanHeader")
    Call<ProductionDetail> getProdPlanHeader(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("getDeptSettingValue")
    Call<Configure> getDeptSettingValue(@Query("settingKeyList") String settingKeyList);

    @POST("getSfPlanDetailForBom")
    Call<DeptDetail> getSfPlanDetailForBom(@Query("headerId") int headerId, @Query("deptId") int deptId);

    @POST("saveBom")
    Call<Info> saveBom(@Body BillOfMaterialHeader billOfMaterialHeader);

    @POST("getBOMHeaderBmsAndStore")
    Call<ShowAllReq> getBOMHeaderBmsAndStore(@Query("fromDept") int fromDate, @Query("toDept") int toDate, @Query("status") ArrayList<Integer> status);

    @POST("getDetailedwithreqId")
    Call<BillOfMaterialHeader> getDetailedwithreqId(@Query("reqId") int reqId);

    @GET("getallBOMHeaderList")
    Call<ShowAllReq> getallBOMHeaderList();

    @POST("getSfPlanDetailForMixing")
    Call<MixingDetail> getSfPlanDetailForMixing(@Query("headerId") int headerId, @Query("deptId") int deptId);

    @POST("getMixingHeaderList")
    Call<MixingHeader> getMixingHeaderList(@Query("frmdate") String fromDate, @Query("todate") String toDate);

    @POST("getDetailedwithMixId")
    Call<MixingDetailList> getDetailedwithMixId(@Query("mixId") int mixId);

    @POST("insertTempMixing")
    Call<Info> insertTempMixing(@Body ArrayList<TempMixing> tempMixing);

    @POST("getTempMixItemDetail")
    Call<GetTempMixItemDetailList> getTempMixItemDetail(@Query("prodHeaderId") int prodHeaderId);

    @POST("insertMixingHeaderndDetailed")
    Call<MixingHeaderDetail> insertMixingHeaderndDetailed(@Body MixingHeaderDetail mixingHeaderDetail);

    @POST("updateisMixingandBom")
    Call<Integer> updateisMixingandBom(@Query("productionId") int productionId,@Query("flag") int flag,@Query("deptId") int deptId);

    @POST("showDetailsForCp")
    Call<DeptDetail> showDetailsForCp(@Query("headerId") int headerId,@Query("deptId") int deptId);

    @POST("getSfItemDetailsApp")
    Call<SfItemHeader> getSfItemDetailsApp(@Query("sfId") int sfId, @Query("rmQty") float rmQty);

    @POST("showDetailsForCoating")
    Call<DeptDetail> showDetailsForCoating(@Query("headerId") int headerId,@Query("deptId") int deptId);

    @POST("getSfItemDetailsForCreamPrep")
    Call<SfItemHeader> getSfItemDetailsForCreamPrep(@Query("sfId") int sfId);

    @POST("showDetailsForLayering")
    Call<DeptDetail> showDetailsForLayering(@Query("headerId") int headerId,@Query("deptId") int deptId);

    @GET("showMiniSubCatList")
    Call<ArrayList<Group>> showMiniSubCatList();

    @POST("findItemsByGrpIdForRmIssue")
    Call<ArrayList<Item>> findItemsByGrpIdForRmIssue(@Query("itemGrp3") int itemGrp3, @Query("prodHeaderId") int prodHeaderId,@Query("fromDept") int fromDept,@Query("toDept") int toDept);

    @POST("getSfDetailsForIssue")
    Call<DeptDetail> getSfDetailsForIssue(@Query("headerId") int headerId, @Query("deptId") int deptId,@Query("itemId") String itemId);

    @POST("getSfType")
    Call<ArrayList<Type>> getSfType(@Query("delStatus") int delStatus);

    @POST("getItemSfHeadersBySfType")
    Call<ArrayList<Production>> getItemSfHeadersBySfType(@Query("sfType") int sfType);

    @GET("login")
    Call<Login> login(@Query("username") String username,@Query("password") String password);

    @POST("PostProdPlanHeaderwithDetailed")
    Call<PostProductionPlanHeader> PostProdPlanHeaderwithDetailed(@Query("planHeaderId") int planHeaderId);

    @POST("showDetailItemLayering")
    Call<DeptDetail> showDetailItemLayering(@Query("headerId") int headerId, @Query("rmId") int rmId,@Query("deptId") int deptId);

    @POST("getBOMHeaderListBmsAndStore")
    Call<ShowAllReq> getBOMHeaderListBmsAndStore(@Query("frmdate") String frmdate, @Query("todate") String todate, @Query("fromDept") int fromDept, @Query("toDept") int toDept);

    @POST("getBOMHeaderList")
    Call<ShowAllReq> getBOMHeaderList(@Query("frmdate") String frmdate, @Query("todate") String todate, @Query("bmsDeptId") int bmsDeptId, @Query("mixDeptId") int mixDeptId);

    @POST("getBmsStockHeader")
    Call<Stock> getBmsStockHeader(@Query("status") int status, @Query("rmType") int rmType, @Query("deptId") int deptId);

    @POST("getCurentBmsStockRM")
    Call<StockDetail> getCurentBmsStockRM(@Query("deptId") int deptId,@Query("stockDate") String stockDate);

    @POST("getCurentBmsStockSF")
    Call<StockDetailSf> getCurentBmsStockSF(@Query("deptId") int deptId, @Query("stockDate") String stockDate);

    @POST("getBmsStockRMBetDate")
    Call<StockDetail> getBmsStockRMBetDate(@Query("fromDate") String fromDate,@Query("toDate") String toDate);

    @POST("getBmsStockSFBetDate")
    Call<StockDetailSf> getBmsStockSFBetDate(@Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @POST("updateBmsStockForRM")
    Call<Info> updateBmsStockForRM(@Body UpdateBmsStockList updateBmsStockList);

    @POST("insertBmsStock")
    Call<BmsStockHeader> insertBmsStock(@Body BmsStockHeader bmsStockHeader);

    @POST("updateBmsStockForSF")
    Call<Info> updateBmsStockForSF(@Body UpdateBmsSfStockList updateBmsSfStockList);
}
