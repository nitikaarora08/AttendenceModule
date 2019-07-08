package com.patanjali.attendencemodule.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientInstance {


    private static Retrofit retrofit;
    public static final String BASE_URL = "http://1.6.145.44/Modern_trade/index.php/";

    static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*/


            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                   // .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;

    }

    public static ApiInterface getApiClient(){
        return  getRetrofitInstance().create(ApiInterface.class);

    }

}
