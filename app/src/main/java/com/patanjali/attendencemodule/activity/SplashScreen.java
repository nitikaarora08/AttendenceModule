
package com.patanjali.attendencemodule.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;


/**
 * Created by Patanjali on 16-10-2018.
 */

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        boolean connected= SplashScreen.Connetion(getApplicationContext());

        if (connected==true)

        {

        }
        else

            {

            }

       // FirebaseApp.initializeApp(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
                    if (sharedPrefrenceUtil.getLoginStatus())
                    {

                        Intent mainIntent = new Intent(SplashScreen.this, HomeActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                    else

                    {
                        Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();

                    }




                }


                }, SPLASH_DISPLAY_LENGTH);

    }

    public static boolean Connetion(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;

        }

        else

            return false;
    }

}



