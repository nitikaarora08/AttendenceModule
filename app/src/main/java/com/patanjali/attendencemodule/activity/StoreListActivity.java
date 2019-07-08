package com.patanjali.attendencemodule.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.adapter.CustomAdpter;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.StoreListRequest;
import com.patanjali.attendencemodule.model.StoreListResponse;
import com.patanjali.attendencemodule.model.UserStoreList;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;
import com.patanjali.attendencemodule.service.LocationMotironingService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ProgressDialog mprogressDialog;

    TextView textView_name,employee_code;

    ImageView image_back,img_back;
    String storeid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list);

        textView_name=(TextView)findViewById(R.id.textView_name);
        employee_code=(TextView)findViewById(R.id.employee_code);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent1= getIntent();
        storeid= intent1.getStringExtra("storeid");
        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        textView_name.setText(sharedPrefrenceUtil.getEmployeeCode());
        employee_code.setText(sharedPrefrenceUtil.getName());

        image_back=(ImageView)findViewById(R.id.img_back);

        image_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(StoreListActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }

        });

        SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
        if (sharedPrefrenceUtil.getCheckIn())

        {
            //productlist.setAlpha(500
            Calendar callcheck = Calendar.getInstance();
            DateFormat dffcheck = new SimpleDateFormat("yyyy/MM/dd");
            String datecheck= dffcheck.format(callcheck.getTime());
            if (sharedPrefrenceUtil1.getCheckInTime().equals(datecheck)) {
                sharedPrefrenceUtil.setCheckInService(true);
                try {
                    //Create a new PendingIntent and add it to the AlarmManager
                    Intent intent = new Intent(this, LocationMotironingService.class);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        PendingIntent pendingIntent = PendingIntent.getForegroundService(this,
                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager am =
                                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
                    }
                    else
                        {
                        PendingIntent pendingIntent = PendingIntent.getService(this,
                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager am =
                                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);

                        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),

                                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
                        }

                }

                catch (Exception e) {


                }
            }

        }

        getStoreList();

    }

    public void getStoreList()

    {

        if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

                /* if (!lat.equals("0.0") && !longi.equals("0.0")) {*/

            showLoadingDialog(this);
            final String err = "Something went wrong please try again";
            SharedPrefrenceUtil sharedPrefrenceUtil= new SharedPrefrenceUtil(getApplicationContext());


            StoreListRequest loginRequestt = new StoreListRequest("4912");

            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();

            mapiClinet.dostorelist(loginRequestt).enqueue(new Callback<StoreListResponse>() {

                    @Override
                    public void onResponse(Call<StoreListResponse> call, Response<StoreListResponse> response) {
                        hideLoading();
                        if (response.body() != null) {
                            Boolean value = response.body().getStatus();

                            if (value.equals(true)) {
                                //everthing is ok

                                //showMessage("uplaoded successfully");

                                StoreListResponse storeListResponse = response.body();
                                List<UserStoreList> userStoreLists= storeListResponse.getUserStoreList();

                               // Toast.makeText(getApplicationContext(),userStoreLists.get(1).getStoreName(),Toast.LENGTH_LONG).show();
                                CustomAdpter customAdpter = new CustomAdpter(StoreListActivity.this,userStoreLists);
                                recyclerView.setAdapter(customAdpter);

                            }

                            else

                                {
                                //showTaost(msg);

                            }


                        }

                        else {

                            Toast.makeText(getApplicationContext(), "Something Went Wrong....Please Try Again", Toast.LENGTH_LONG).show();
                        }
                        //  showTaost(msg);
                    }

                    @Override
                    public void onFailure(Call<StoreListResponse> call, Throwable t) {
                        hideLoading();
                        showMessage(err);
                    }
                });
               /* } else

                showMessage("Please Wait While we are Fetching your Location and Try Again!!");*/
            }

            else
            showMessage(getString(R.string.no_internet));


    }

    public void showLoadingDialog(Context context) {
        mprogressDialog = new ProgressDialog(context);
        mprogressDialog.show();
        if (mprogressDialog.getWindow() != null) {
            mprogressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mprogressDialog.setContentView(R.layout.progress_layout);
        mprogressDialog.setIndeterminate(true);
        mprogressDialog.setCancelable(false);
        mprogressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(StoreListActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    public void hideLoading() {
        if (mprogressDialog != null && mprogressDialog.isShowing()) {
            mprogressDialog.cancel();

        }
    }

    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
