package com.patanjali.attendencemodule;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.patanjali.attendencemodule.activity.HomeActivity;
import com.patanjali.attendencemodule.activity.StoreListActivity;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.EmpCheckinResponseModel;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.service.LocationMotironingService;
import com.patanjali.attendencemodule.service.NewLocationMotironingService;
import com.patanjali.attendencemodule.singletonclass.SingletonClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patanjali on 05-03-2019.
 */

public class CheckInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    Button takepicture;
    ProgressDialog mprogressDialog;
    Realm realm;
    File imageFile;

    Bitmap photo;

    GoogleApiClient mLocationClient;
    ImageView imagepicture;

    String[] attendence_typevalues = {"Select Type", "Half Day", "Full Day"};

    String[] type_of_visit_value = {"Select Visit", "Office Visit", "Office Meeting", "Field Visit", "Training"};

    Spinner attendence, typeofvisit;

    boolean image_value = true;

    Button textView_save;

    String date, time;

    Boolean checkservicestart = false;

    LocationManager locationManager;
    private static final int CAMERA_REQUEST = 1;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int LOCATION_REQUEST = 99;
    TextView textView_checkin, employee_codecheckin;

    String attendencetypeitem, typeofvisititem;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_layout);

        attendence = (Spinner) findViewById(R.id.attendence);
        typeofvisit = (Spinner) findViewById(R.id.typeofvisit);
        employee_codecheckin = (TextView) findViewById(R.id.employee_codecheckin);
        textView_checkin = (TextView) findViewById(R.id.textView_checkin);

        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        textView_checkin.setText(sharedPrefrenceUtil.getEmployeeCode());
        employee_codecheckin.setText(sharedPrefrenceUtil.getName());
        Calendar call = Calendar.getInstance();

        DateFormat dff = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dfff = new SimpleDateFormat("HH:mm:ss");

        date = dff.format(call.getTime());
        time = dfff.format(call.getTime());

        textView_save = (Button) findViewById(R.id.textView_save);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initiateLocationClient();

        attendence = (Spinner) findViewById(R.id.attendence);
        typeofvisit = (Spinner) findViewById(R.id.typeofvisit);
        imagepicture = (ImageView) findViewById(R.id.imagepicture);
        takepicture = (Button) findViewById(R.id.takepicture);

        getStaticDataSpinner();
        getSelectedItemValue();

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkCameraPermission()) {
                    ActivityCompat.requestPermissions(CheckInActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }

            }

        });

        textView_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
                String empcode = sharedPrefrenceUtil.getName();

                if (attendencetypeitem.equals("Select Type")) {

                    Toast.makeText(getApplicationContext(), "Please Select Attendence Type", Toast.LENGTH_LONG).show();

                }

                else if (typeofvisititem.equals("Select Visit")) {

                    Toast.makeText(getApplicationContext(), "Please Select the Type Of Visit", Toast.LENGTH_LONG).show();
                }

                else {

                    doEmpCheckIn(empcode, attendencetypeitem, typeofvisititem, SingletonClass.getInstance().getLatitude()+"",
                            SingletonClass.getInstance().getLongitude() + "", time, date, SingletonClass.getInstance().getAddress()+"");

                }
            }
        });

    }

    private void getSelectedItemValue() {

        attendence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int po, long id) {

                attendencetypeitem = attendence_typevalues[po];
                // Toast.makeText(getApplicationContext(),state_name,Toast.LENGTH_SHORT).show()

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                // your code here

            }

        });

        typeofvisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int po, long id) {

                // Toast.makeText(getApplicationContext(),state_name,Toast.LENGTH_SHORT).show()
                typeofvisititem = type_of_visit_value[po];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                // your code here

            }
        });

    }

    void doEmpCheckIn(String empCode, String applyFor, String attFor, String lat, String longi, String time, final String date,String address) {
        if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {
            if (imageFile != null) {

                if (!lat.equals("0.0") && !longi.equals("0.0")) {
                    showLoadingDialog(this);
                    final String err = "Something went wrong please try again";
                    RequestBody emp_code = RequestBody.create(MediaType.parse("text/plain"), empCode);
                    RequestBody latlong_address = RequestBody.create(MediaType.parse("text/plain"), address);
                    RequestBody apply_for = RequestBody.create(MediaType.parse("text/plain"), applyFor);
                    RequestBody att_for = RequestBody.create(MediaType.parse("text/plain"), attFor);
                    RequestBody checkin_latitude = RequestBody.create(MediaType.parse("text/plain"), lat);
                    RequestBody checkin_longitude = RequestBody.create(MediaType.parse("text/plain"), longi);
                    RequestBody checkin_time = RequestBody.create(MediaType.parse("text/plain"), time);
                    RequestBody checkin_date = RequestBody.create(MediaType.parse("text/plain"), date);
                    RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), date);
                    RequestBody requestBodyProfile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    MultipartBody.Part profile_img = MultipartBody.Part.createFormData("profile_pic", imageFile.getName(), requestBodyProfile);
                    SingletonClass.getInstance().getApiClient().doEmpCheckinn(emp_code, apply_for, att_for, checkin_latitude,
                            checkin_longitude, checkin_time, checkin_date, remarks, profile_img,latlong_address).enqueue(new Callback<EmpCheckinResponseModel>() {

                        @Override
                        public void onResponse(Call<EmpCheckinResponseModel> call, Response<EmpCheckinResponseModel> response) {
                            hideLoading();
                            if (response.body() != null) {
                                Boolean value = response.body().getStatus();
                                if (value.equals(true)) {

                                    SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
                                    sharedPrefrenceUtil.setCheckIn(true);
                                    sharedPrefrenceUtil.setCheckInTime(date);
                                    //everthing is ok
                                    showMessage("uplaoded successfully");
                                    stopService(new Intent(getApplication(), NewLocationMotironingService.class));
                                    Intent i = new Intent(CheckInActivity.this, StoreListActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    showMessage("Not uplaodedddddd");
                                    //showTaost(msg);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Something Went Wrong....Please Try Again", Toast.LENGTH_LONG).show();
                            }
                            //  showTaost(msg);
                        }

                        @Override
                        public void onFailure(Call<EmpCheckinResponseModel> call, Throwable t) {
                            hideLoading();
                            showMessage(err);
                        }
                    });
                } else {

                    showMessage("Please Wait While we are Fetching your Location and Try Again!!");
                    ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), NewLocationMotironingService.class));
                }
                }
            else

                showMessage(getString(R.string.no_file_chosen));

        } else
            showMessage(getString(R.string.no_internet));
    }

    private void initiateLocationClient() {
        mLocationClient = new GoogleApiClient.Builder(CheckInActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationClient.connect();
    }

    void startLocationService() {

        if (!LocationMotironingService.isServiceRunning) {
            //Toast.makeText(getApplicationContext(),"Trueeeeeeeeeeeeeeeeee",Toast.LENGTH_LONG).show();
           // startService(new Intent(CheckInActivity.this, LocationMotironingService.class));
            ContextCompat.startForegroundService(CheckInActivity.this, new Intent(getApplicationContext(), LocationMotironingService.class));

        }

    }

    public void checkGpsProvider() {

        if (mLocationClient != null) {
            if (mLocationClient.isConnected()) {

                int permissionLocation = ContextCompat.checkSelfPermission(CheckInActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        startLocationService();

                    }

                    else {

                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(8000);
                        locationRequest.setFastestInterval(6000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);

                        builder.setAlwaysShow(true);

                        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

                        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                                .checkLocationSettings(mLocationClient, builder.build());
                        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                            @Override
                            public void onResult(LocationSettingsResult result) {

                                final Status status = result.getStatus();
                                switch (status.getStatusCode()) {
                                    case LocationSettingsStatusCodes.SUCCESS:
                                        // Log.d(TAG, "success provider");

                                        startLocationService();

                                        break;
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                                        startLocationService();
                                        //   Toast.makeText(CheckInActivity.this,"aaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();

                                        //Log.d(TAG, "success required");
                                        // Location settings are not satisfied.
                                        // But could be fixed by showing the user a dialog.
                                        try {
                                            // Show the dialog by calling startResolutionForResult(),
                                            // and check the result in onActivityResult().
                                            // Ask to turn on GPS automatically

                                            status.startResolutionForResult(CheckInActivity.this,
                                                    REQUEST_CHECK_SETTINGS_GPS);
                                        }

                                        catch (IntentSender.SendIntentException e) {
                                            //  Log.d(TAG,"error 2");

                                            // Ignore the error.
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        //                                   Log.d(TAG, "success unavailble");
                                        // Location settings are not satisfied.
                                        // However, we have no way
                                        // to fix the
                                        // settings so we won't show the dialog.
                                        // finish();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (NetworkUtility.checkPermissions(CheckInActivity.this, new String[]
                {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST)) {

            checkGpsProvider();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkCameraPermission() {

        int result1 = ContextCompat.checkSelfPermission(CheckInActivity.this, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(CheckInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(CheckInActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  Toast.makeText(CheckInActivity.this, "fragment toast ", Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                boolean granted = true;

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        granted = false;
                        break;

                    }
                }

                if (granted) {

                    //   Toast.makeText(getActivity(),"givennnnn",Toast.LENGTH_SHORT).show();

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

                break;

            case LOCATION_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getActivity(),"pppppp",Toast.LENGTH_SHORT).show();
                    checkGpsProvider();

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == RESULT_OK) {

                //Toast.makeText(getActivity(),"aaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();

                photo = (Bitmap) data.getExtras().get("data");
                imagepicture.setImageBitmap(photo);
                Uri tempUri = getImageUri(CheckInActivity.this, photo);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                imageFile = new File(getRealPathFromURI(tempUri));
               /* date_timee.setText(date_time);
                //farmername.clearFocus();
                farmer_no_children.requestFocus();
                SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getContext());
                sharedPrefrenceUtil.setDateTime(date_time);*/

               /* Toast.makeText(getActivity(), "Image Capture Successfully!", Toast.LENGTH_SHORT).show();
                //System.out.println(mImageCaptureUri);
                Bitmap immage = photo;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                sharedPrefrenceUtil.setName(imageEncoded);*/

            }

        } else if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            if (resultCode == RESULT_OK) {
                //   Toast.makeText(CheckInActivity.this, "aaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
                startLocationService();

            }
        }

    }

    private void getStaticDataSpinner() {
        {

            ArrayAdapter mAdapter = new ArrayAdapter<String>(CheckInActivity.this, R.layout.support_simple_spinner_dropdown_item, attendence_typevalues) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Cast the spinner collapsed item (non-popup item) as a text view
                    TextView tv = (TextView) super.getView(position, convertView, parent);
                    // Set the text color of spinner item
                    tv.setTextColor(Color.GRAY);
                    // Return the view
                    return tv;

                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    // Cast the drop down items (popup items) as text view
                    TextView tv = (TextView) super.getDropDownView(position, convertView, parent);

                    // Set the text color of drop down items
                    tv.setTextColor(Color.BLACK);

                    // If this item is selected item
                    if (position == 0) {
                        // Set spinner selected popup item's text color
                        tv.setTextColor(Color.GRAY);
                    }

                    // Return the modified view
                    return tv;
                }
            };

            //Setting the ArrayAdapter data on the Spinner
            attendence.setAdapter(mAdapter);

        }
        {
            ArrayAdapter typeofvisitadapter = new ArrayAdapter<String>(CheckInActivity.this, R.layout.support_simple_spinner_dropdown_item, type_of_visit_value) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Cast the spinner collapsed item (non-popup item) as a text view
                    TextView tv = (TextView) super.getView(position, convertView, parent);

                    // Set the text color of spinner item
                    tv.setTextColor(Color.GRAY);
                    // Return the view
                    return tv;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    // Cast the drop down items (popup items) as text view
                    TextView tv = (TextView) super.getDropDownView(position, convertView, parent);

                    // Set the text color of drop down items
                    tv.setTextColor(Color.BLACK);

                    // If this item is selected item
                    if (position == 0) {

                        // Set spinner selected popup item's text color
                        tv.setTextColor(Color.GRAY);

                    }

                    // Return the modified view
                    return tv;
                }
            };

            //Setting the ArrayAdapter data on the Spinner
            typeofvisit.setAdapter(typeofvisitadapter);


        }
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

    public void hideLoading() {
        if (mprogressDialog != null && mprogressDialog.isShowing()) {
            mprogressDialog.cancel();
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (CheckInActivity.this.getContentResolver() != null) {
            Cursor cursor = CheckInActivity.this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }

        return path;
    }

    @Override
    public void onBackPressed() {

    SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

    if (sharedPrefrenceUtil.getCheckIn())
    {

        Intent i = new Intent(CheckInActivity.this, HomeActivity.class);
        startActivityForResult(i,2);
        finish();
    }
    else
    {

        Intent i = new Intent(CheckInActivity.this, HomeActivity.class);
       startActivity(i);
       finish();
    }


    }
}
