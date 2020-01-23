package com.example.bms_app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bms_app.R;
import com.example.bms_app.adapter.ExpandableListAdapter;
import com.example.bms_app.constants.Constants;
import com.example.bms_app.fragment.BMSListFragment;
import com.example.bms_app.fragment.BMSProductionFragment;
import com.example.bms_app.fragment.CotingCreamFragment;
import com.example.bms_app.fragment.CreamPreparationFragment;
import com.example.bms_app.fragment.IssueFromBMSFragment;
import com.example.bms_app.fragment.IssusFragment;
import com.example.bms_app.fragment.LayeringCreamFragment;
import com.example.bms_app.fragment.MainFragment;
import com.example.bms_app.fragment.ManualProdFragment;
import com.example.bms_app.fragment.MixingProductionFragment;
import com.example.bms_app.fragment.MixingProductionListFragment;
import com.example.bms_app.fragment.MixingStockFragment;
import com.example.bms_app.fragment.RequestForMixingFragment;
import com.example.bms_app.fragment.RequestForStoreFragment;
import com.example.bms_app.fragment.ShowAllRequestFragment;
import com.example.bms_app.fragment.ShowRequestBOMFragment;
import com.example.bms_app.model.Configure;
import com.example.bms_app.model.FrItemStockConfigure;
import com.example.bms_app.model.Login;
import com.example.bms_app.model.MenuModel;
import com.example.bms_app.utils.CommonDialog;
import com.example.bms_app.utils.CustomSharedPreference;
import com.example.bms_app.utils.PermissionsUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private List<FrItemStockConfigure> frItemStockConfiguresList;
    public Login loginUser;
    int bmsId=0,mixId=0;
    String bmsName,mixName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (PermissionsUtil.checkAndRequestPermissions(MainActivity.this)) {
        }

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSettingValue("MIX");
        getSettingValueBMS("BMS");

        try {
            String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("HOME_ACTIVITY : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        View header = navigationView.getHeaderView(0);

        TextView tvNavHeadName = header.findViewById(R.id.tvNavHeadName);
        LinearLayout linearLayout_header = header.findViewById(R.id.linearLayout_header);
        TextView tvNavHeadDesg = header.findViewById(R.id.tvNavHeadDesg);
        //CircleImageView ivNavHeadPhoto = header.findViewById(R.id.ivNavHeadPhoto);

        if (loginUser != null) {
            tvNavHeadName.setText("" + loginUser.getUser().getUsername());

            linearLayout_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new MainFragment(), "Exit");
                    ft.commit();
                }
            });

        // tvNavHeadDesg.setText("" + loginUser.getEmpDeptName());

//            try {
//                Picasso.with(HomeActivity.this).load(Constants.IMAGE_URL + "" + loginUser.getEmpPhoto()).placeholder(getResources().getDrawable(R.drawable.profile)).into(ivNavHeadPhoto);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        if (loginUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new MainFragment(), "Exit");
        ft.commit();
    }


    private void getSettingValue(String mix) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+mix);
        if (Constants.isOnline(getApplicationContext())) {
            final CommonDialog commonDialog = new CommonDialog(getApplicationContext(), "Loading", "Please Wait...");
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
                            Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSettingValueBMS(String bms) {
        Log.e("PARAMETER","                 SETTING KEY VALUES     "+bms);
        if (Constants.isOnline(getApplicationContext())) {
            final CommonDialog commonDialog = new CommonDialog(getApplicationContext(), "Loading", "Please Wait...");
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
                            Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Configure> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment exit = getSupportFragmentManager().findFragmentByTag("Exit");
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("MainFragment");
        Fragment bmsListFragment = getSupportFragmentManager().findFragmentByTag("BMSListFragment");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exit instanceof MainFragment && exit.isVisible()) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(MainActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);


        } else if (homeFragment instanceof RequestForMixingFragment && homeFragment.isVisible() ||
                homeFragment instanceof RequestForStoreFragment && homeFragment.isVisible() ||
                homeFragment instanceof ShowAllRequestFragment && homeFragment.isVisible() ||
                homeFragment instanceof ShowRequestBOMFragment && homeFragment.isVisible() ||
                homeFragment instanceof MixingProductionFragment && homeFragment.isVisible() ||
                homeFragment instanceof MixingProductionListFragment && homeFragment.isVisible() ||
                homeFragment instanceof MixingStockFragment && homeFragment.isVisible() ||
                homeFragment instanceof BMSProductionFragment && homeFragment.isVisible() ||
                homeFragment instanceof IssueFromBMSFragment && homeFragment.isVisible() ||
                homeFragment instanceof BMSListFragment && homeFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MainFragment(), "Exit");
            ft.commit();
        }else if (bmsListFragment instanceof CreamPreparationFragment && bmsListFragment.isVisible() ||
                bmsListFragment instanceof LayeringCreamFragment && bmsListFragment.isVisible() ||
                bmsListFragment instanceof CotingCreamFragment && bmsListFragment.isVisible() ||
                bmsListFragment instanceof IssusFragment && bmsListFragment.isVisible() ||
                bmsListFragment instanceof ManualProdFragment && bmsListFragment.isVisible()) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new BMSListFragment(), "MainFragment");
            ft.commit();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {

//        MenuModel menuModel = new MenuModel("Android WebView Tutorial", true, false, "https://www.journaldev.com/9333/android-webview-example-tutorial"); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel menuModel;
        MenuModel childModel;

        Login login = null;
        try {
            String userStr = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_USER);
            Gson gson = new Gson();
            login = gson.fromJson(userStr, Login.class);
            Log.e("HOME_ACTIVITY : ", "--------USER-------" + loginUser);
            Log.e("HOME : ", "--------LOGIN-------" + login);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.e("LOGIN","-----------------------------------------------USER---------------------------------"+loginUser);

      //  if(login.getUser().getDeptId()==16 || login.getUser().getDeptId()==9 || login.getUser().getDeptId()==10 ) {
             menuModel = new MenuModel("Production Department", true, true, ""); //Menu of Java Tutorials
            headerList.add(menuModel);

            childModel = new MenuModel("Request for Mixing", false, false, "Request for Mixing");
            childModelsList.add(childModel);

            childModel = new MenuModel("Request for Stored", false, false, "Request for Stored");
            childModelsList.add(childModel);

            childModel = new MenuModel("Show all Request", false, false, "Show all Request");
            childModelsList.add(childModel);


            if (menuModel.hasChildren) {
                Log.d("API123", "here");
                childList.put(menuModel, childModelsList);
            }
    //   }

       // if(login.getUser().getDeptId()==16 || login.getUser().getDeptId()==9 || login.getUser().getDeptId()==10 ) {
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel("Mixing Department", true, true, "Mixing Department"); //Menu of Python Tutorials
            headerList.add(menuModel);
            childModel = new MenuModel("Request from Production", false, false, "Request from Production");
            childModelsList.add(childModel);

            childModel = new MenuModel("Add Mixing", false, false, "Add Mixing");
            childModelsList.add(childModel);

            childModel = new MenuModel("Mixing Production List", false, false, "Mixing Production List");
            childModelsList.add(childModel);

            childModel = new MenuModel("Mixing Stock", false, false, "Mixing Stock");
            childModelsList.add(childModel);

            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
      //  }
       // if(login.getUser().getDeptId()==16 || login.getUser().getDeptId()==11 ) {
            childModelsList = new ArrayList<>();
            menuModel = new MenuModel("BMS Department", true, true, "BMS Department"); //Menu of Python Tutorials
            headerList.add(menuModel);

            childModel = new MenuModel("Add BMS Production", false, false, "Add BMS Production");
            childModelsList.add(childModel);

             childModel = new MenuModel("BMS Production List", false, false, "BMS Production List");
             childModelsList.add(childModel);

             childModel = new MenuModel("Issue from BMS", false, false, "Issue from BMS");
             childModelsList.add(childModel);

             childModel = new MenuModel("BMS Stock", false, false, "BMS Stock");
            childModelsList.add(childModel);


            if (menuModel.hasChildren) {
                childList.put(menuModel, childModelsList);
            }
       // }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Logout", true, true, "Logout"); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Logout", false, false, "Logout");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {

                    }
                }

                if (!headerList.get(groupPosition).isGroup) {
                    Log.e("Header ------------- ", " ---" + headerList.get(groupPosition).getUrl());

                    String url = headerList.get(groupPosition).getUrl();

                    if (url.equalsIgnoreCase("Request for Mixing")) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new RequestForMixingFragment(), "MainFragment");
                        ft.commit();
                    }

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
                        return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.getUrl().equalsIgnoreCase("Request for Mixing")) {
                        Fragment adf = new RequestForMixingFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if (model.getUrl().equalsIgnoreCase("Request for Stored")) {
                        Fragment adf = new RequestForStoreFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if (model.getUrl().equalsIgnoreCase("Show all Request")) {
                        Fragment adf = new ShowAllRequestFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if(model.getUrl().equalsIgnoreCase("Request from Production"))
                    {
                        Fragment adf = new ShowRequestBOMFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if(model.getUrl().equalsIgnoreCase("Add Mixing"))
                    {
                        Fragment adf = new MixingProductionFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if(model.getUrl().equalsIgnoreCase("Mixing Production List"))
                    {
                        Fragment adf = new MixingProductionListFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }if(model.getUrl().equalsIgnoreCase("Add BMS Production"))
                    {
                        Fragment adf = new BMSListFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }
                    if(model.getUrl().equalsIgnoreCase("Mixing Stock"))
                    {
                        Fragment adf = new MixingStockFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        args.putInt("strDept", mixId);
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }
                    if(model.getUrl().equalsIgnoreCase("BMS Stock"))
                    {
                        Fragment adf = new MixingStockFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        args.putInt("strDept", bmsId);
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }
                    if(model.getUrl().equalsIgnoreCase("BMS Production List"))
                    {
                        Fragment adf = new BMSProductionFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }
                    if(model.getUrl().equalsIgnoreCase("Issue from BMS"))
                    {
                        Fragment adf = new IssueFromBMSFragment();
                        Bundle args = new Bundle();
                        args.putString("slugName", model.getUrl());
                        adf.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "MainFragment").commit();
                    }
                    if(model.getUrl().equalsIgnoreCase("Logout"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure ! you want to logout?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CustomSharedPreference.deletePreference(MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
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
                    }

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
    }
}
