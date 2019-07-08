package com.patanjali.attendencemodule.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.OTPVerifyRequest;
import com.patanjali.attendencemodule.model.OTPVerifyResponse;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patanjali on 16-10-2018.
 */

public class VerifyingOTP extends AppCompatActivity {


    EditText editText1, editText2, editText3, editText4;

    String mdevice_id;
    String deviceid;
    String token,otp;
    BroadcastReceiver broadcastReceiver;


    private static final int PERMISSION_REQUEST_CODE = 200;


    //    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
//        return result == PackageManager.PERMISSION_GRANTED;
//    }

//    private void requestPermission() {
//
//        ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
//
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifying_otp);
//        token = FirebaseInstanceId.getInstance().getToken();

        editText1 = (EditText) findViewById(R.id.otp1);
        editText2 = (EditText) findViewById(R.id.otp2);
        editText3 = (EditText) findViewById(R.id.otp3);
        editText4 = (EditText) findViewById(R.id.otp4);
        //2023


        boolean connected= SplashScreen.Connetion(getApplicationContext());
        if (connected==true) {
        }
        else


        {
            AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
            alert.setMessage("No Internet Connection!!");
            alert.setPositiveButton("OK", null);
            alert.show();
        }
//        requestPermission();

        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              //  Toast.makeText(getApplicationContext(),"broadcast",Toast.LENGTH_LONG).show();

//                Toast.makeText(context,"testtttttttt",Toast.LENGTH_LONG).show();
                Bundle data  = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for(int i=0;i<pdus.length;i++){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
                    String messageBody = smsMessage.getMessageBody();
                    otp=messageBody.replaceAll("[^0-9]","");   // otp abcd contains otp
//            which is in number format
                    //Pass on the text to our listener.
//            if(b==true) {
//                mListener.messageReceived(abcd);  // attach value to interface
//                object
//            }
//            else
//            {
//            }
                    //ed.setText(abcd);
                    String s1,s2,s3,s4;
                    s1=otp.substring(0,1);
                    s2=otp.substring(1,2);
                    s3=otp.substring(2,3);
                    s4=otp.substring(3,4);

                    Log.e("OTP",otp+" s1 : "+s1+" s2: " +s2+" s3: "+s3+" s4: "+s4);
                    editText1.setText(s1);
                    editText2.setText(s2);
                    editText3.setText(s3);
                    editText4.setText(s4);

                    deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    //postData();
                    verifyOtp();

                }

            }
        };


        registerReceiver(broadcastReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (editText1.length()>0) {

                    editText1.clearFocus();
                    //    Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG).show();
                    editText2.requestFocus();
                }



                //mdevice_id=getDeviceId();
                //Toast.makeText(VerifyingOTP.this,deviceid,Toast.LENGTH_LONG).show();


//        return "ASBHDCJ1234";
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (editText2.length()>0) {
                    editText2.clearFocus();
                    editText3.requestFocus();
                    //    Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG).show();


                    //mdevice_id=getDeviceId();
                    //Toast.makeText(VerifyingOTP.this,deviceid,Toast.LENGTH_LONG).show();

                }
//        return "ASBHDCJ1234";
            }

        });
        editText3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (editText3.length() > 0) {
                    editText3.clearFocus();
                    //    Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG).show();
                    editText4.requestFocus();

                    //mdevice_id=getDeviceId();
                    //Toast.makeText(VerifyingOTP.this,deviceid,Toast.LENGTH_LONG).show();


//        return "ASBHDCJ1234";
                }
            }
        });


        editText4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //    Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG).show();

                deviceid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                //postData();
                verifyOtp();

                //mdevice_id=getDeviceId();
                //Toast.makeText(VerifyingOTP.this,deviceid,Toast.LENGTH_LONG).show();


//        return "ASBHDCJ1234";
            }
        });
    }
            private void verifyOtp() {

                String Str1 = editText1.getText().toString();
                String Str2 = editText2.getText().toString();
                String str3 = editText3.getText().toString();
                String str4 = editText4.getText().toString();
                String temp_pin = Str1 + Str2 + str3 + str4;
                Intent intent = getIntent();
                String stored_pincode = intent.getStringExtra("otp");

                if (stored_pincode != null && stored_pincode.equals(temp_pin)) {

                    Toast.makeText(getApplicationContext(),"Successfully Verified", Toast.LENGTH_LONG).show();

                   // SharedPrefrenceUtil sharedPrefrenceUtil= new SharedPrefrenceUtil(getApplicationContext());

                    //sharedPrefrenceUtil.setLoginStatus(true);
                      postData();

                } else {
                    if (!Str1.isEmpty() && (!Str2.isEmpty()) && (!str3.isEmpty()) && (!str4.isEmpty())) {
                        Toast.makeText(VerifyingOTP.this, getString(R.string.wrong_pincode_txt), Toast.LENGTH_SHORT).show();
                    }
                }
            }

    public  void postData() {

        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        String empcode= sharedPrefrenceUtil.getName();

        OTPVerifyRequest otpVerifyRequest = new OTPVerifyRequest(deviceid,empcode);
        ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
        mapiClinet.verifyotp(otpVerifyRequest).enqueue(new Callback<OTPVerifyResponse>() {

            @Override
            public void onResponse(@NonNull Call<OTPVerifyResponse> call, @NonNull Response<OTPVerifyResponse> response) {
                if (response.body() != null) {
                    Boolean status = response.body().getStatus();
                    //String otp = response.body().getOtp();
                    if (status.equals(true)) {

                         SharedPrefrenceUtil sharedPrefrenceUtil= new SharedPrefrenceUtil(getApplicationContext());

                        sharedPrefrenceUtil.setLoginStatus(true);
                        Toast.makeText(getApplicationContext(),"Login Success", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(VerifyingOTP.this, HomeActivity.class);
                        startActivity(intent1);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_LONG).show();
                        //showTaost(msg);
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"PLs enter the", Toast.LENGTH_LONG).show();
                }
                //  showTaost(msg);
            }
            @Override
            public void onFailure(Call<OTPVerifyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "notttttttttttt", Toast.LENGTH_LONG).show();
                //showTaost("somwthing wet w
                //
                // rong,try later");
            }
        });
    }
}
