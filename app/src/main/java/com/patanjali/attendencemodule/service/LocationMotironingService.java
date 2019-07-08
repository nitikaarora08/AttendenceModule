package com.patanjali.attendencemodule.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.AddressModel;
import com.patanjali.attendencemodule.model.CheckOurRequest;
import com.patanjali.attendencemodule.model.CheckOutResponse;
import com.patanjali.attendencemodule.model.DateTimeModel;
import com.patanjali.attendencemodule.model.LatitudeModel;
import com.patanjali.attendencemodule.model.LongitudeModel;
import com.patanjali.attendencemodule.model.MultipleLatLonRequest;
import com.patanjali.attendencemodule.model.MultipleLatLongResponse;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;
import com.patanjali.attendencemodule.singletonclass.SingletonClass;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;


/**
 * Created by Patanjali on 20-10-2018.
 */


  public class LocationMotironingService extends Service implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final int ID_SERVICE = 101;

    public static boolean isServiceRunning = false;

    Geocoder geocoder;
    List<Address> addresses= new ArrayList<>();;

    Realm realm;
    List<LatitudeModel> results= new ArrayList<>();
    boolean image_value=true;

    ImageView sammary;

    String address;

    List<LongitudeModel> longitudeModel=new ArrayList<>();

    List<DateTimeModel> datetimelist=new ArrayList<>();
    List<AddressModel> addressModels=new ArrayList<>();
    private static final String TAG = LocationMotironingService.class.getSimpleName();
    private final int UPDATE_INTERVAL = 1900000;
    //1800000
    private final int FASTEST_INTERVAL = 1800000;

    GoogleApiClient mLocationClient;

    LocationRequest mLocationRequest;


    void startUpdates() {

        Log.d(TAG, "api_client " + checkPermission());
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,
                mLocationRequest, this);

    }


    private void initiateLocationClient() {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest = LocationRequest.create()

                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                //.setSmallestDisplacement(100.0f)
                .setFastestInterval(FASTEST_INTERVAL);

        mLocationClient.connect();

    }

    void stopLOcationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);

    }

    @Override
    public void onConnected(Bundle dataBundle) {

        if (checkPermission())

            try {
                startUpdates();
            }
            catch (Exception e)
            {

            }


    }

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    //to get the location chan
    // ge
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed " + location);

        if (location != null) {

           // Toast.makeText(getApplicationContext(),""+SingletonClass.getInstance().getLongitude(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),""+SingletonClass.getInstance().getLatitude(),Toast.LENGTH_LONG).show();
            //SingletonClass.getInstance().setLatitude(location.getLatitude());
            Log.d(TAG, " latitude : " + location.getLatitude());
            Log.d(TAG, " longitude : " + location.getLongitude());

            SingletonClass.getInstance().setLatitude(location.getLatitude());
            SingletonClass.getInstance().setLongitude(location.getLongitude());
            SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
            Calendar call = Calendar.getInstance();
            DateFormat dff = new SimpleDateFormat("yyyy/MM/dd");
            String date= dff.format(call.getTime());

            try
            {

                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                Toast.makeText(getApplicationContext(),""+address,Toast.LENGTH_LONG).show();
                SingletonClass.getInstance().setAddress(address);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

            }


            catch (IOException e) {
                e.printStackTrace();
            }

            if (sharedPrefrenceUtil.getCheckInTime().equals(date) && sharedPrefrenceUtil.getCheckIn()) {

                Utils.sendNotification(getApplicationContext());
                geocoder = new Geocoder(this, Locale.getDefault());

                Calendar calll = Calendar.getInstance();
                DateFormat dfff = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss");
                String date_time = dfff.format(calll.getTime());
                Realm.init(getApplicationContext());
                Realm realm = Realm.getDefaultInstance();

                //          Toast.makeText(getApplicationContext(), "" + date_time, Toast.LENGTH_LONG).show();
                //Begin Transaction

                realm.beginTransaction();

                realm.where(LongitudeModel.class).findAll();

                LatitudeModel student = realm.createObject(LatitudeModel.class);
                student.setLatitude(location.getLatitude());
                LongitudeModel longitudeModel = realm.createObject(LongitudeModel.class);
                longitudeModel.setLongitude(location.getLongitude());
                DateTimeModel dateTimeModel = realm.createObject(DateTimeModel.class);
                dateTimeModel.setDatetime(date_time);
                AddressModel addressModel = realm.createObject(AddressModel.class);
                addressModel.setAddress(address);
                realm.commitTransaction();

                if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {
                    //Toast.makeText(getApplicationContext(),"Testinggg",Toast.LENGTH_LONG).show();
                    postCheckoutdata();

                }

                else
                    {

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


          /*  if (sharedPrefrenceUtil.getCheckIn())
            {
                Toast.makeText(getApplicationContext(),"data test data",Toast.LENGTH_LONG).show();
            }*/

            }

            else

                {

                if (sharedPrefrenceUtil.getCheckIn())

                {
                    if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {


                        postCheckOutSingle();


                    }

                    else

                        {

                        Constraints constraints = new Constraints.Builder()
                                // you can add as many constraints as you want
                                .setRequiredNetworkType(NetworkType.CONNECTED)

                                //.setRequiresCharging(true)
                                .build();

                        final OneTimeWorkRequest workRequest =

                                new OneTimeWorkRequest.Builder(CheckOutWorkerService.class)
                                        .setConstraints(constraints)
                                        .addTag("single")
                                        .build();

                            UUID myWorkId = workRequest.getId();

                        WorkManager.getInstance().enqueue(workRequest);

                    }

                }

                else

                    {

                }

            }

          /*  if (sharedPrefrenceUtil.getCheckIn())
            {
                Toast.makeText(getApplicationContext(),"data test data",Toast.LENGTH_LONG).show();
            }*/

        }
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onCreate() {

        Log.d("checkmethod", "checkkkk");
      //  Toast.makeText(getApplicationContext(),"xxxxxxxxxxxxxx"+SingletonClass.getInstance().getLongitude(),Toast.LENGTH_LONG).show();

        //   Toast.makeText(getApplicationContext(),"Service started....",Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(),"Create Newwwww  Foreground Service Started",Toast.LENGTH_LONG).show();

        Realm.init(getApplicationContext());
        isServiceRunning = true;
        initiateLocationClient();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(ID_SERVICE, notification);

        super.onCreate();

    }

    @Override
    public void onDestroy() {

        Log.d("detroy checkmethod", "checkkkk");
// \       Toast.makeText(getApplicationContext(),"destroyyyyyyy",Toast.LENGTH_LONG).show();
        super.onDestroy();
        isServiceRunning = false;
        //SingletonClass.getInstance().setLongitude(0);
        //SingletonClass.getInstance().setLatitude(0);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //   Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Service Stop")
                .setContentText("Stop Service")
                .setSmallIcon(R.drawable.bell_icon)
                .setSound(defaultSoundUri)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
        //stopLOcationUpdates();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // Toast.makeText(getApplicationContext(),"Testinggg",Toast.LENGTH_LONG).show();


        initiateLocationClient();
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    public void postCheckoutdata() {

        Calendar call = Calendar.getInstance();
        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date_time = dff.format(call.getTime());
        final SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        String empcode = sharedPrefrenceUtil.getName();
        //  showLoadingDialog(this);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        results = realm.where(LatitudeModel.class).findAll();

        longitudeModel = realm.where(LongitudeModel.class).findAll();

        datetimelist = realm.where(DateTimeModel.class).findAll();

        addressModels = realm.where(AddressModel.class).findAll();
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

        final List<String> addressList = new ArrayList<>();
        for (AddressModel model : addressModels) {
            addressList.add(model.getAddress() + "");
        }

        final List<String> multipledatetimelist = new ArrayList<>();
        for (DateTimeModel model : datetimelist) {
            multipledatetimelist.add(model.getDatetime() + "");
        }

        if (SingletonClass.getInstance().getLatitude() != 0) {
            MultipleLatLonRequest loginRequestt = new MultipleLatLonRequest(addressList,empcode, latList, longList, date_time, multipledatetimelist);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.docheckout(loginRequestt).enqueue(new Callback<MultipleLatLongResponse>() {

                @Override
                public void onResponse(@NonNull Call<MultipleLatLongResponse> call, @NonNull Response<MultipleLatLongResponse> response) {

                    // hideLoading();
                    if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

                        if (response.body() != null) {
                            Boolean status = response.body().getStatus();
                            //  int as = response.code();

                            if (status.equals(true)) {
                                //stopService(new Intent(getApplicationContext(), LocationMotironingService.class));
                                Toast.makeText(getApplicationContext(), "Your Location Successfully Send to Server", Toast.LENGTH_LONG).show();
                                //   RealmResults<LatitudeModel> results = realm.where(LatitudeModel.class).findAll();
                                realm.beginTransaction();

                                realm.delete(LatitudeModel.class);

                                realm.delete(LongitudeModel.class);

                                realm.delete(DateTimeModel.class);

                                realm.delete(AddressModel.class);
                                //results.deleteAllFromRealm();
                                realm.commitTransaction();

                            }

                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Something Went Wrong...Try Again!!", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "PLs Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<MultipleLatLongResponse> call, Throwable t) {

                    //  hideLoading();
                    Toast.makeText(getApplicationContext(), "Something Went Wrong....", Toast.LENGTH_LONG).show();
                    // Toast.makeText(getApplicationContext(),"Something Went Wrong .....Try Again", Toast.LENGTH_LONG).show();

                }

            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please Wait While we are Fetching the Location",Toast.LENGTH_LONG).show();

            ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), NewLocationMotironingService.class));


        }


    }

    public void postCheckOutSingle()
    {

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        Calendar call = Calendar.getInstance();
        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat timeonly = new SimpleDateFormat("HH:mm:ss");
        String date_time = dff.format(call.getTime());
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
                                sharedPrefrenceUtil.setCheckInTime(" ");
                                SharedPrefrenceUtil sharedPrefrenceUtil1 = new SharedPrefrenceUtil(getApplicationContext());
                                sharedPrefrenceUtil1.setCheckIn(false);
                                sharedPrefrenceUtil.setCheckDataSammary(false);
                                stopService(new Intent(getApplication(), LocationMotironingService.class));
                                stopService(new Intent(getApplication(), NewLocationMotironingService.class));
                                WorkManager.getInstance().cancelAllWorkByTag("multiple");
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

                                                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                                        am.cancel(pendingIntent);

                                    }

                                    else

                                        {

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
                                realm.delete(AddressModel.class);
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

