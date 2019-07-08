package com.patanjali.attendencemodule.network;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Patanjali on 20-10-2018.
 */

public class NetworkUtility {
    public static boolean isNetworkAvailable(Context context){
        boolean isNetworkConencted=false;
        ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnected()){
            isNetworkConencted=true;
        }
        return  isNetworkConencted;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissions(final Context context, final String[] permissions, final int permissionCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        boolean flag = false;

        if (currentAPIVersion >= Build.VERSION_CODES.M) {

            try {
                for (String permsn : permissions) {
                    if (ContextCompat.checkSelfPermission(context, permsn) != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                        break;
                  /*  if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permsn)) {

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, permissions, permissionCode);
                    }*/

                    } else {
                        flag = true;
                    }
                    if (!flag)
                        break;
                }
                if (!flag) {
                   /* AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {*/
                    ActivityCompat.requestPermissions((Activity) context, permissions, permissionCode);
                        /*}
                    });*/
                 /*   AlertDialog alert = alertBuilder.create();
                    alert.show();
                    alert.setCanceledOnTouchOutside(false);*/
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            flag = true;

        }
        return flag;

    }

}
