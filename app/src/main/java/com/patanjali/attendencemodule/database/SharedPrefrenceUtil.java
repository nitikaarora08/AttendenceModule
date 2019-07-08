package com.patanjali.attendencemodule.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Patanjali on 10-10-2018.
 */

public class SharedPrefrenceUtil {
    Context context;

    public SharedPrefrenceUtil(Context context) {
        this.context = context;
    }

public void setName(String name)
{

    SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

    SharedPreferences.Editor editor= sharedPreferences.edit();

    editor.putString("Name",name);
    editor.commit();
}
    public String getName()
{

    SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

     return sharedPreferences.getString("Name","");
}

    public void setCheckInTime(String name)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("checkintime",name);
        editor.commit();
    }
    public String getCheckInTime()
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("checkintime","");
    }

    public void setCheckInDate(String name)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("checkintime",name);
        editor.commit();
    }
    public String getCheckInDate()
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("checkintime","");
    }



    public void setCheckInDateTime(String name)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("checkintime",name);
        editor.commit();
    }
    public String getCheckInDateTime()
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("checkintime","");
    }

    public void setDataSammary(String datasammary)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("datasammary",datasammary);
        editor.commit();
    }
    public String getDataSammary()
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("datasammary","");
    }

    public void setEmployeeCode(String employeeCode)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("EmployeeCode",employeeCode);
        editor.commit();

    }
    public String getEmployeeCode()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("EmployeeCode","");
    }

    public void setQuantity(String setQuantity)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("setQuantity",setQuantity);
        editor.commit();

    }

    public String getQuantity()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getString("setQuantity","");
    }

    public void setLoginStatus(boolean value)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean("IsLogin",value);
        editor.commit();
    }
    public boolean getLoginStatus()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("IsLogin",false);
    }
    public void setCheckIn(boolean checkIn)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean("IsCheckIN",checkIn);
        editor.commit();
    }
    public boolean getCheckIn()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("IsCheckIN",false);
    }

    public void setCheckInService(boolean checkIn)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean("IsCheckIN",checkIn);
        editor.commit();
    }
    public boolean getCheckInService()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("IsCheckIN",false);
    }

    public void setBackValue(boolean backValue)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean("BackCheckIn",backValue);
        editor.commit();
    }
    public boolean getBackValue()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("BackCheckIn",false);
    }


    public void setCheckDataSammary(boolean imagevalue)
    {

        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putBoolean("setCheckDataSammary",imagevalue);
        editor.commit();
    }
    public boolean getCheckDataSammary()
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("Data",Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("setCheckDataSammary",false);
    }





}
