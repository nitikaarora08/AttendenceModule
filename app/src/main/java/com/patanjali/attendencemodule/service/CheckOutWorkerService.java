package com.patanjali.attendencemodule.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.CheckOurRequest;
import com.patanjali.attendencemodule.model.CheckOutResponse;
import com.patanjali.attendencemodule.model.DateTimeModel;
import com.patanjali.attendencemodule.model.LatitudeModel;
import com.patanjali.attendencemodule.model.LongitudeModel;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;
import com.patanjali.attendencemodule.singletonclass.SingletonClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patanjali on 23-12-2018.
 */

public class CheckOutWorkerService extends Worker {


    public CheckOutWorkerService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

     ArrayList<String> machinedataa = new ArrayList<>();
    ArrayList<String> saveCheckedItem = new ArrayList<>();

    private static final int ID_SERVICE = 101;

    public static boolean isServiceRunning = false;

    private static final String TAG = LocationMotironingService.class.getSimpleName();

    private final int UPDATE_INTERVAL = 180000000;
    //1800000
    private final int FASTEST_INTERVAL = 170000000;
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;

    ProgressDialog mprogressDialog;
    String date_time;

    TextView employeename,text_checkin;

    TextView employee_code;

    Realm realm;
    List<LatitudeModel> results= new ArrayList<>();
    boolean image_value=true;

    ImageView sammary;

    final List<String> latList = new ArrayList<>();
    List<LongitudeModel> longitudeModel=new ArrayList<>();

    List<DateTimeModel> datetimelist=new ArrayList<>();

    final List<String> multipledatetimelist = new ArrayList<>();
    final List<String> longList = new ArrayList<>();


    @NonNull
    @Override
    public Result doWork() {

       // PostFarmerDetail();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(),"Check Out Successfully",Toast.LENGTH_LONG).show();
                Log.d("checkwork","worktime");
                //Your UI code here
            }
        });
        //isServiceRunning = true;
        postCheckOutSingle();
        return Result.success();

    }

    public void postCheckOutSingle() {

        Calendar call = Calendar.getInstance();
        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat timeonly = new SimpleDateFormat("HH:mm:ss");
        String date_time = dff.format(call.getTime());
        String time = timeonly.format(call.getTime());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                Realm.init(getApplicationContext());
                realm = Realm.getDefaultInstance();

                results = realm.where(LatitudeModel.class).findAll();
                longitudeModel = realm.where(LongitudeModel.class).findAll();
                datetimelist = realm.where(DateTimeModel.class).findAll();

                for (LatitudeModel model : results) {
                    // latList.add(0,"ffff");
                    latList.add(model.getLatitude() + "");
                    Log.d("latlist", "" + latList);

                }

                for (LongitudeModel model : longitudeModel) {
                    longList.add(model.getLongitude() + "");
                }

                for (DateTimeModel model : datetimelist) {
                    multipledatetimelist.add(model.getDatetime() + "");
                }

            }

        });


       /* if (SingletonClass.getInstance().getLongitude() != 0.0) {*/

//            Toast.makeText(getApplicationContext(), SingletonClass.getInstance().getLatitude() + "", Toast.LENGTH_LONG).show();
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
                                sharedPrefrenceUtil.setCheckInTime("");
                                SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                                sharedPrefrenceUtil1.setCheckIn(false);
                                sharedPrefrenceUtil.setCheckDataSammary(false);
                                getApplicationContext().stopService(new Intent(getApplicationContext(), LocationMotironingService.class));
                                getApplicationContext().stopService(new Intent(getApplicationContext(), NewLocationMotironingService.class));
                                //WorkManager.getInstance().cancelAllWorkByTag("multiple");
                                WorkManager.getInstance().cancelAllWorkByTag("single");
                                //Toast.makeText(getApplicationContext(), "Multiple CheckOut Successssfully Sent to Server", Toast.LENGTH_LONG).show();
                                //checkin.setVisibility(View.VISIBLE);
                                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();
                                try {

                                    //Create a new PendingIntent and add it to the AlarmManager
                                    Intent intent = new Intent(getApplicationContext(), LocationMotironingService.class);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        PendingIntent pendingIntent = PendingIntent.getForegroundService(getApplicationContext(),
                                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager am =
                                                (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);

                                        am.cancel(pendingIntent);

                                    }

                                    else {

                                        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager am =

                                                (AlarmManager) getApplicationContext().getSystemService(Activity.ALARM_SERVICE);

                                        am.cancel(pendingIntent);

                                    }

                                } catch (Exception e) {

                                }


                              /*  realm.beginTransaction();
                                realm.delete(LatitudeModel.class);
                                realm.delete(LongitudeModel.class);
                                realm.delete(DateTimeModel.class);
                                //results.deleteAllFromRealm();
                                realm.commitTransaction();*/
                                //checkin.setVisibility(View.VISIBLE);

                            }
                        } else {
                      //      Toast.makeText(getApplicationContext(), "Response Is Null.....Try Again!!", Toast.LENGTH_LONG).show();

                        }

                    } else {
                    //    Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<CheckOutResponse> call, Throwable t) {

                  //  Toast.makeText(getApplicationContext(), "Something Went Wrong....Try Again!!", Toast.LENGTH_LONG).show();


                }

            });
      /*  }
        else
        {

//            Toast.makeText(getApplicationContext(), "Please Wait!!... we are Fetching the Location!!", Toast.LENGTH_LONG).show();

            ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), NewLocationMotironingService.class));

        }*/
        }

}

