package com.patanjali.attendencemodule.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.patanjali.attendencemodule.CheckInActivity;
import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.OTPRequest;
import com.patanjali.attendencemodule.model.OTPResponse;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECEIVE_SMS;


/**
 * Created by Patanjali on 11-10-2018.
 */

public class LoginActivity extends AppCompatActivity {
    String empcode;
    EditText editText_empcode;

    GoogleApiClient mLocationClient;
    public static final int LOCATION_REQUEST = 99;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 100;

    LocationManager locationManager;

    ProgressDialog mprogressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String str1, str2, str3;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION,RECEIVE_SMS}, LOCATION_REQUEST);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(this);
        requestPermission();

        editText_empcode = (EditText) findViewById(R.id.user_email_editbox);

    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    public void sendot(View view) {
        //  if (checkPermission() && checkPermissioncamer()) {
       /* PowerManager pm = (PowerManager) getApplicationContext().getSystemService(
                Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations("com.patanjali.moderntrade")) {*/

           // Toast.makeText(getApplicationContext(), "Please Enter the OTP", Toast.LENGTH_LONG).show();
            showLoadingDialog(LoginActivity.this);
            empcode = editText_empcode.getText().toString();
            OTPRequest loginRequest = new OTPRequest(empcode);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.doLogin(loginRequest).enqueue(new Callback<OTPResponse>() {

                @Override
                public void onResponse(@NonNull Call<OTPResponse> call, @NonNull Response<OTPResponse> response) {

                    hideLoading();

                    // String msg=response.body().getError_msg();

                    if (response.body() != null) {

                        Boolean errCode = response.body().getStatus();

                        String otp = response.body().getOtp();

                        OTPResponse otpResponse = response.body();

                        if (errCode.equals(true)) {

                            String employee_code = response.body().getEmpDetail().get(0).getEmpCode();
                            String employee = response.body().getEmpDetail().get(0).getEmpName();
                            SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
                            sharedPrefrenceUtil.setName(employee_code);
                            sharedPrefrenceUtil.setEmployeeCode(employee);
                            // Toast.makeText(getApplicationContext(), "Please Enter the OTP", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, VerifyingOTP.class);
                            i.putExtra("otp", otp);
                            Log.e("otp", otp);
                            startActivity(i);
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Mobile Number is not Valid...Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
                        // Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                //  showTaost(msg);


                @Override
                public void onFailure(Call<OTPResponse> call, Throwable t) {

                    hideLoading();
                    Toast.makeText(getApplicationContext(), "Something went Wrong...Try Again", Toast.LENGTH_LONG).show();
                    //showTaost("somwthing wet wrong,try later");
                }

            });

       /* } else {


            Intent myIntent = new Intent();
            myIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivity(myIntent);
        }*/
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
}
