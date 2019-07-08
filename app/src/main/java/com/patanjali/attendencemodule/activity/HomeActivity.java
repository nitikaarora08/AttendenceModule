package com.patanjali.attendencemodule.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.patanjali.attendencemodule.CheckInActivity;
import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.CheckOurRequest;
import com.patanjali.attendencemodule.model.CheckOutRequest;
import com.patanjali.attendencemodule.model.CheckOutResponse;
import com.patanjali.attendencemodule.model.DataSammaryRequest;
import com.patanjali.attendencemodule.model.DataSammaryResponse;
import com.patanjali.attendencemodule.model.DateTimeModel;
import com.patanjali.attendencemodule.model.LatitudeModel;
import com.patanjali.attendencemodule.model.LongitudeModel;
import com.patanjali.attendencemodule.model.MultipleLatLonRequest;
import com.patanjali.attendencemodule.model.MultipleLatLongResponse;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;
import com.patanjali.attendencemodule.service.LocationMotironingService;
import com.patanjali.attendencemodule.service.NewLocationMotironingService;
import com.patanjali.attendencemodule.singletonclass.SingletonClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.work.WorkManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Patanjali on 07-03-2019.
 */

public class HomeActivity extends AppCompatActivity {


    ImageView checkin,logout;

    ProgressDialog mprogressDialog;

    String date_time;

    TextView employeename,text_checkin;

    TextView employee_code;

    Realm realm;
    List<LatitudeModel> results= new ArrayList<>();
    boolean image_value=true;

    ImageView sammary,storelist,productlist;

    List<LongitudeModel> longitudeModel=new ArrayList<>();

    List<DateTimeModel> datetimelist=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        text_checkin=(TextView)findViewById(R.id.text_checkin);

        storelist=(ImageView)findViewById(R.id.storelist);
        //productlist=(ImageView)findViewById(R.id.productlist);
        employeename=(TextView) findViewById(R.id.textView_name);
        employee_code=(TextView)findViewById(R.id.employee_code);

        final SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

        if (sharedPrefrenceUtil.getCheckInService())

        {

            if (!LocationMotironingService.isServiceRunning) {

                //Toast.makeText(getApplicationContext(),"Trueeeeeeeeeeeeeeeeee",Toast.LENGTH_LONG).show();
                // startService(new Intent(CheckInActivity.this, LocationMotironingService.class));
                ContextCompat.startForegroundService(HomeActivity.this, new Intent(getApplicationContext(), LocationMotironingService.class));

            }

        }

        employeename.setText(sharedPrefrenceUtil.getEmployeeCode());

        employee_code.setText(sharedPrefrenceUtil.getName());

        sammary=(ImageView)findViewById(R.id.sammary);

        sammary.setAlpha(80);

        Calendar call = Calendar.getInstance();

        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        date_time = dff.format(call.getTime());


        checkin=(ImageView)findViewById(R.id.checkin);

        logout=(ImageView)findViewById(R.id.logout);
        logout.setAlpha(80);
        storelist.setAlpha(80);
        //productlist.setAlpha(80);

        storelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                if (sharedPrefrenceUtil.getCheckIn()) {
                    Intent i = new Intent(HomeActivity.this, StoreListActivity.class);
                    startActivity(i);
                    finish();

                }

                else
                {

                    Toast.makeText(getApplicationContext(),"Please Check In",Toast.LENGTH_LONG).show();
                }

            }
        });



        SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());

        if (sharedPrefrenceUtil.getCheckIn())
        {

            checkin.setAlpha(80);
            text_checkin.setVisibility(View.VISIBLE);
            sammary.setAlpha(500);
            logout.setAlpha(500);
            storelist.setAlpha(500);
            //productlist.setAlpha(500);

            Calendar callcheck = Calendar.getInstance();
            DateFormat dffcheck = new SimpleDateFormat("yyyy/MM/dd");
            String datecheck= dffcheck.format(callcheck.getTime());

        }

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

                if (sharedPrefrenceUtil.getCheckIn() && sharedPrefrenceUtil.getCheckDataSammary()) {

                   // postCheckoutdata();
                    postCheckOutSingle();

                }

                else

                    {

                        Toast.makeText(getApplicationContext(),"Please Submit the Check In Details....",Toast.LENGTH_LONG).show();
                }
            }

        });

        sammary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPrefrenceUtil sharedPrefrenceUtil2 = new SharedPrefrenceUtil(getApplicationContext());

                if (sharedPrefrenceUtil.getCheckIn())
                {

                    LayoutInflater li = LayoutInflater.from(HomeActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            HomeActivity.this);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);
                    userInput.setText(sharedPrefrenceUtil.getDataSammary());
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            //result.setText(userInput.getText());

                                            sharedPrefrenceUtil.setDataSammary(userInput.getText().toString());
                                            if (!userInput.getText().toString().isEmpty()) {

                                                SendDataSammaryToServer();

                                            }

                                            else
                                                {

                                                    Toast.makeText(getApplicationContext(), "Please Enter the Data Sammary Filed", Toast.LENGTH_LONG).show();
                                                }
                                    }

                    private void SendDataSammaryToServer() {

                        Calendar call = Calendar.getInstance();
                        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String date_time = dff.format(call.getTime());
                                            //date_time="";
                                            SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                                            String empcode = sharedPrefrenceUtil.getName();
                                            DataSammaryRequest loginRequestt = new DataSammaryRequest(userInput.getText().toString(), sharedPrefrenceUtil.getName(), date_time);
                                            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
                                            mapiClinet.dodatasammary(loginRequestt).enqueue(new Callback<DataSammaryResponse>() {

                                                @Override
                                                public void onResponse(@NonNull Call<DataSammaryResponse> call, @NonNull Response<DataSammaryResponse> response) {
                                                    if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

                                                        hideLoading();
                                                        if (response.body() != null) {
                                                            Boolean status = response.body().getStatus();
                                                              int as = response.code();

                                                            if (status.equals(true)) {

                                                                Toast.makeText(getApplicationContext(), "Submitted Successfully..", Toast.LENGTH_LONG).show();

                                                                sharedPrefrenceUtil.setCheckDataSammary(true);

                                                            }

                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Please Try Again.....", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                                                    }

                                                    //  showTaost(msg);
                                                }

                                                @Override
                                                public void onFailure(Call<DataSammaryResponse> call, Throwable t) {
                                                    hideLoading();
                                                    Toast.makeText(getApplicationContext(), "Something went Wrong....Try Again", Toast.LENGTH_LONG).show();

                                                }

                                            });


                                        }
                                    })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();


                                        }

                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } else {

                    Toast.makeText(getApplicationContext(),"Please Check In First",Toast.LENGTH_LONG).show();

                }
            }
        });


        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPrefrenceUtil sharedPrefrenceUtil2 = new SharedPrefrenceUtil(getApplicationContext());

                if (!sharedPrefrenceUtil2.getCheckIn()) {
                    Intent i = new Intent(HomeActivity.this, CheckInActivity.class);
                    startActivity(i);
                    finish();
                }
                else {

                    Toast.makeText(getApplicationContext(),"You Have Already Check In",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

   /* public void postCheckoutdata() {

        final SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        String empcode = sharedPrefrenceUtil.getName();
        showLoadingDialog(this);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        results = realm.where(LatitudeModel.class).findAll();
        longitudeModel = realm.where(LongitudeModel.class).findAll();
        datetimelist = realm.where(DateTimeModel.class).findAll();
        final List<String> latList = new ArrayList<>();

        for (LatitudeModel model : results) {

            // latList.add(0,"ffff");
            latList.add(model.getLatitude() + "");
            Log.d("latlist", "" + latList);

        }

        final List<String> longList = new ArrayList<>();
        for (LongitudeModel model : longitudeModel) {
            longList.add(model.getLongitude() + "");
        }

        final List<String> multipledatetimelist = new ArrayList<>();
        for (DateTimeModel model : datetimelist) {
            multipledatetimelist.add(model.getDatetime() + "");
        }

        if (SingletonClass.getInstance().getLongitude() != 0.0) {
            MultipleLatLonRequest loginRequestt = new MultipleLatLonRequest(empcode, latList, longList, date_time, multipledatetimelist);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.docheckout(loginRequestt).enqueue(new Callback<MultipleLatLongResponse>() {

                @Override
                public void onResponse(@NonNull Call<MultipleLatLongResponse> call, @NonNull Response<MultipleLatLongResponse> response) {

                    hideLoading();
                    if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

                        if (response.body() != null) {

                            Boolean status = response.body().getStatus();
                            //  int as = response.code();

                            if (status.equals(true)) {

                                sharedPrefrenceUtil.setDataSammary("");
                                SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                                sharedPrefrenceUtil1.setCheckIn(false);
                                sharedPrefrenceUtil.setCheckDataSammary(false);
                                stopService(new Intent(getApplication(), LocationMotironingService.class));
                                Toast.makeText(getApplicationContext(), "Multiple CheckOut Successssfully Sent to Server", Toast.LENGTH_LONG).show();
                                //checkin.setVisibility(View.VISIBLE);
                                text_checkin.setVisibility(View.VISIBLE);
                                logout.setAlpha(80);
                                checkin.setAlpha(500);
                                sammary.setAlpha(80);
                                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();
                                realm.beginTransaction();
                                realm.delete(LatitudeModel.class);
                                realm.delete(LongitudeModel.class);
                                realm.delete(DateTimeModel.class);
                                //results.deleteAllFromRealm();
                                realm.commitTransaction();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Something Went Wrong...Try Again!!", Toast.LENGTH_LONG).show();
                          *//*  SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                            sharedPrefrenceUtil1.setCheckIn(false);

                            stopService(new Intent(getApplication(), LocationMotironingService.class));
                            Toast.makeText(getApplicationContext(), "Multiple CheckOut Successssfully Sent to Server", Toast.LENGTH_LONG).show();

                            //checkin.setVisibility(View.VISIBLE);

                            text_checkin.setVisibility(View.VISIBLE);

                            logout.setAlpha(80);

                            checkin.setAlpha(500);

                            sammary.setAlpha(80);

                            //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();

                            realm.beginTransaction();
                            realm.delete(LatitudeModel.class);
                            realm.delete(LongitudeModel.class);
                            realm.delete(DateTimeModel.class);
                            //results.deleteAllFromRealm();
                            realm.commitTransaction();*//*

                            // Toast.makeText(getApplicationContext(), "Something Went Wrong ...Try Again!!", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<MultipleLatLongResponse> call, Throwable t) {

                    hideLoading();
                    Toast.makeText(getApplicationContext(), "Something Went Wrong....", Toast.LENGTH_LONG).show();
                    // Toast.makeText(getApplicationContext(),"Something Went Wrong .....Try Again", Toast.LENGTH_LONG).show();

               *//* SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                sharedPrefrenceUtil1.setCheckIn(false);

                stopService(new Intent(getApplication(), LocationMotironingService.class));
                Toast.makeText(getApplicationContext(), "Multiple CheckOut Successssfully Sent to Server", Toast.LENGTH_LONG).show();

                //checkin.setVisibility(View.VISIBLE);

                text_checkin.setVisibility(View.VISIBLE);

                logout.setAlpha(80);

                checkin.setAlpha(500);

                sammary.setAlpha(80);

                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();

                realm.beginTransaction();
                realm.delete(LatitudeModel.class);
                realm.delete(LongitudeModel.class);
                realm.delete(DateTimeModel.class);
                //results.deleteAllFromRealm();
                realm.commitTransaction();*//*


                }

            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Wait While we are Fetching the Location",Toast.LENGTH_LONG).show();
        }


    }*/

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

    public void hideLoading() {
        if (mprogressDialog != null && mprogressDialog.isShowing()) {
            mprogressDialog.cancel();
        }
    }
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            //checkin.setVisibility(View.GONE);

            checkin.setAlpha(80);

           // int id = getResources().getIdentifier("yourpackagename:drawable/" + R.drawable.daysammary, null, null);
            //sammary.setImageResource(id);

            text_checkin.setVisibility(View.VISIBLE);


        }
    }

    public void postCheckOutSingle()
    {
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        Calendar call = Calendar.getInstance();
        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date_time = dff.format(call.getTime());
        DateFormat timeonly = new SimpleDateFormat("HH:mm:ss");
        String time = timeonly.format(call.getTime());

        if (SingletonClass.getInstance().getLongitude() != 0.0) {

            Toast.makeText(getApplicationContext(), SingletonClass.getInstance().getLatitude() + "", Toast.LENGTH_LONG).show();
            final SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
            String empcode = sharedPrefrenceUtil.getName();
            CheckOurRequest loginRequestt = new CheckOurRequest(SingletonClass.getInstance().getAddress(),sharedPrefrenceUtil.getCheckInTime() +" "+time, SingletonClass.getInstance().getLatitude() + "", SingletonClass.getInstance().getLongitude() + "", empcode);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.docheckoutsingle(loginRequestt).enqueue(new Callback<CheckOutResponse>() {

                @Override
                public void onResponse(@NonNull Call<CheckOutResponse> call, @NonNull Response<CheckOutResponse> response) {
                    if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

                        if (response.body() != null) {

                            Boolean status = response.body().getStatus();
                            //  int as = response.code();
                            if (status.equals(true)) {

                                Toast.makeText(getApplicationContext(), "Check Out Successfully .....", Toast.LENGTH_LONG).show();
                                sharedPrefrenceUtil.setDataSammary("");
                                SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                                sharedPrefrenceUtil1.setCheckIn(false);
                                sharedPrefrenceUtil.setCheckDataSammary(false);
                                sharedPrefrenceUtil.setCheckInTime(" ");
                                stopService(new Intent(getApplication(), LocationMotironingService.class));
                                stopService(new Intent(getApplication(), NewLocationMotironingService.class));

                                WorkManager.getInstance().cancelAllWorkByTag("multiple");
                                WorkManager.getInstance().cancelAllWorkByTag("single");
                                //Toast.makeText(getApplicationContext(), "Multiple CheckOut Successssfully Sent to Server", Toast.LENGTH_LONG).show();
                                //checkin.setVisibility(View.VISIBLE);
                                text_checkin.setVisibility(View.VISIBLE);
                                logout.setAlpha(80);
                                checkin.setAlpha(500);
                                sammary.setAlpha(80);
                                storelist.setAlpha(80);

                                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();

                                try {

                                    //Create a new PendingIntent and add it to the AlarmManager
                                    Intent intent = new Intent(getApplicationContext(), LocationMotironingService.class);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        PendingIntent pendingIntent = PendingIntent.getForegroundService(getApplicationContext(),
                                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                        AlarmManager am =

                                                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);


                                        am.cancel(pendingIntent);
                                    } else {

                                        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager am =

                                                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);

                                        am.cancel(pendingIntent);

                                    }

                                } catch (Exception e) {

                                }

                                realm.beginTransaction();
                                realm.delete(LatitudeModel.class);
                                realm.delete(LongitudeModel.class);
                                realm.delete(DateTimeModel.class);
                                //results.deleteAllFromRealm();
                                realm.commitTransaction();
                                //checkin.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Response Is Null.....Try Again!!", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<CheckOutResponse> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "Something Went Wrong....Try Again!!", Toast.LENGTH_LONG).show();


                }

            });
        }
        else
        {

            Toast.makeText(getApplicationContext(), "Please Wait!!... we are Fetching the Location!!", Toast.LENGTH_LONG).show();

            ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), NewLocationMotironingService.class));

        }
    }

}

