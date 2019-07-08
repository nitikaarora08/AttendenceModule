package com.patanjali.attendencemodule.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
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
import com.patanjali.attendencemodule.model.AddressModel;
import com.patanjali.attendencemodule.model.DateTimeModel;
import com.patanjali.attendencemodule.model.LatitudeModel;
import com.patanjali.attendencemodule.model.LongitudeModel;
import com.patanjali.attendencemodule.model.MultipleLatLonRequest;
import com.patanjali.attendencemodule.model.MultipleLatLongResponse;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
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

public class WorkerService extends Worker {


    public WorkerService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

     ArrayList<String> machinedataa = new ArrayList<>();
    ArrayList<String> saveCheckedItem = new ArrayList<>();

    private static final int ID_SERVICE = 101;

    public static boolean isServiceRunning = false;

    private static final String TAG = LocationMotironingService.class.getSimpleName();

    private final int UPDATE_INTERVAL = 1700000;
    //1800000
    private final int FASTEST_INTERVAL =1600000;
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;

    ProgressDialog mprogressDialog;
    String date_time;

    TextView employeename,text_checkin;

    TextView employee_code;

    Realm realm;

    List<LatitudeModel> results= new ArrayList<>();

    final List<String> addressList = new ArrayList<>();

    List<AddressModel> addressModels=new ArrayList<>();
    List<Address> addresses;
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
           //     Toast.makeText(getApplicationContext(),"Multiple Lat Long Send to Server Sucessfully..",Toast.LENGTH_LONG).show();
                Log.d("checkwork","worktime");
                //Your UI code here
            }

        });
        //isServiceRunning = true;
        postCheckoutdata();
        return Result.success();

    }

    public void postCheckoutdata() {

        Calendar call = Calendar.getInstance();
        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       String date_time = dff.format(call.getTime());
        final SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        String empcode = sharedPrefrenceUtil.getName();
        //  showLoadingDialog(this);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        results = realm.where(LatitudeModel.class).findAll();
        longitudeModel = realm.where(LongitudeModel.class).findAll();
        datetimelist = realm.where(DateTimeModel.class).findAll();

        addressModels = realm.where(AddressModel.class).findAll();

        for (LatitudeModel model : results) {
            // latList.add(0,"ffff");
            latList.add(model.getLatitude() + "");
            Log.d("latlist", "" + latList);

        }

        for (LongitudeModel model : longitudeModel) {
            longList.add(model.getLongitude() + "");
        }

        for (AddressModel model : addressModels) {
                    addressList.add(model.getAddress() + "");
                }
        for (DateTimeModel model : datetimelist) {
            multipledatetimelist.add(model.getDatetime() + "");
        }
            }

        });

       /* if (SingletonClass.getInstance().getLatitude() != 0) {*/
        MultipleLatLonRequest loginRequestt = new MultipleLatLonRequest(addressList,empcode, latList, longList, date_time, multipledatetimelist);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.docheckout(loginRequestt).enqueue(new Callback<MultipleLatLongResponse>() {

                @Override
                public void onResponse(@NonNull Call<MultipleLatLongResponse> call, @NonNull Response<MultipleLatLongResponse> response) {

                    // hideLoading();
                  /*  if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {*/

                    if (response.body() != null) {

                            Boolean status = response.body().getStatus();
                            //  int as = response.code();
                            if (status.equals(true)) {
                                //stopService(new Intent(getApplicationContext(), LocationMotironingService.class));
                                Toast.makeText(getApplicationContext(), "Your Location Successfully Send to Server", Toast.LENGTH_LONG).show();
                                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();
                                WorkManager.getInstance().cancelAllWorkByTag("multiple");

                                realm.beginTransaction();
                                realm.delete(LatitudeModel.class);
                                realm.delete(LongitudeModel.class);
                                realm.delete(DateTimeModel.class);
                                realm.delete(AddressModel.class);
                                //results.deleteAllFromRealm();
                                realm.commitTransaction();


                            }

                        } else {
                            //Toast.makeText(getApplicationContext(), "Something Went Wrong...Try Again!!", Toast.LENGTH_LONG).show();


                            Constraints constraints = new Constraints.Builder()
                                    // you can add as many constraints as you want
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    //.setRequiresCharging(true)
                                    .build();

                            final OneTimeWorkRequest workRequest =

                                    new OneTimeWorkRequest.Builder(WorkerService.class)
                                            .setConstraints(constraints)
                                            .addTag("multiple")
                                            .build();

                            UUID myWorkId = workRequest.getId();
                            WorkManager.getInstance().enqueue(workRequest);
                        }

                   /* } else {
                        //Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }*/

                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<MultipleLatLongResponse> call, Throwable t) {

                    //  hideLoading();
                    //Toast.makeText(getApplicationContext(), "Something Went Wrong....", Toast.LENGTH_LONG).show();


                    Constraints constraints = new Constraints.Builder()
                            // you can add as many constraints as you want
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            //.setRequiresCharging(true)
                            .build();

                    final OneTimeWorkRequest workRequest =

                            new OneTimeWorkRequest.Builder(WorkerService.class)
                                    .setConstraints(constraints)
                                    .addTag("multiple")
                                    .build();

                    UUID myWorkId = workRequest.getId();
                    WorkManager.getInstance().enqueue(workRequest);

                    // Toast.makeText(getApplicationContext(),"Something Went Wrong .....Try Again", Toast.LENGTH_LONG).show();

                }

            });
        /* else {
            Toast.makeText(getApplicationContext(), "Please Wait While we are Fetching the Location", Toast.LENGTH_LONG).show();

        }*/
    }
}

