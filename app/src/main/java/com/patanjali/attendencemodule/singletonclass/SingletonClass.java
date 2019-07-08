package com.patanjali.attendencemodule.singletonclass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SingletonClass {

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double latitude, longitude;

    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static SingletonClass getmInsatnce() {
        return mInsatnce;
    }

    public static void setmInsatnce(SingletonClass mInsatnce) {
        SingletonClass.mInsatnce = mInsatnce;
    }

    static private SingletonClass mInsatnce;

    private SingletonClass() {

    }

    synchronized public static SingletonClass getInstance() {
        if (mInsatnce == null)
            mInsatnce = new SingletonClass();
        return mInsatnce;
    }

    public ApiInterface getApiClient(){

        return getRetrofitInstance(getGson(getGsonBuilder())).create(ApiInterface.class);
    }

    Gson getGson(GsonBuilder builder){
        return builder.setLenient().create();
    }

    GsonBuilder getGsonBuilder(){
        return  new GsonBuilder();
    }

    Retrofit getRetrofitInstance(Gson gson){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(RetrofitClientInstance.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;

    }

}