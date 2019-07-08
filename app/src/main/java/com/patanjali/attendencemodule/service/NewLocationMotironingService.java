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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
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
import com.patanjali.attendencemodule.model.CheckOutRequest;
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

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;


/**
 * Created by Patanjali on 20-10-2018.
 */


  public class NewLocationMotironingService extends Service implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final int ID_SERVICE = 102;

    public static boolean isServiceRunning = false;

    Geocoder geocoder;
    List<Address> addresses= new ArrayList<>();;

    String address;

    Realm realm;
    List<LatitudeModel> results= new ArrayList<>();
    boolean image_value=true;

    ImageView sammary;

    List<LongitudeModel> longitudeModel=new ArrayList<>();

    List<DateTimeModel> datetimelist=new ArrayList<>();

    private static final String TAG = NewLocationMotironingService.class.getSimpleName();

    private final int UPDATE_INTERVAL = 3000;

    //1800000

    private final int FASTEST_INTERVAL = 2000;
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

            startUpdates();

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
               // Toast.makeText(getApplicationContext(),""+address,Toast.LENGTH_LONG).show();
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

}