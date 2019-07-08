package com.patanjali.attendencemodule.activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.adapter.CustomAdpter;
import com.patanjali.attendencemodule.adapter.CustomItemListAdpter;
import com.patanjali.attendencemodule.adapter.CustomListAdapter;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.ItemListResponse;
import com.patanjali.attendencemodule.model.PostProducrListResponse;
import com.patanjali.attendencemodule.model.PostProductListRequest;
import com.patanjali.attendencemodule.model.PostStoreMaterialList;
import com.patanjali.attendencemodule.model.StoreMaterialList;
import com.patanjali.attendencemodule.model.UserStoreList;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.NetworkUtility;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ProductList extends AppCompatActivity {

    ListView listview;
    ProgressDialog mprogressDialog;
    TextView items,textView_namee,employee_codee;
   public ArrayList<String> product= new ArrayList<>();
    public ArrayList<String> materiallist= new ArrayList<>();
    EditText itemquantity;
    ImageView img_save,imageView_backk;
   public HashMap<String, String> adapterMap= new HashMap<>();
    String storeid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storeitem_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        img_save=(ImageView)findViewById(R.id.image_save);
        imageView_backk=(ImageView)findViewById(R.id.imageView_backk);
        employee_codee=(TextView)findViewById(R.id.employee_code);
        textView_namee=(TextView)findViewById(R.id.textView_name);
        itemquantity=(EditText)findViewById(R.id.itemquantity);
        items=(TextView)findViewById(R.id.items);
        listview=(ListView) findViewById(R.id.listview);
        Intent intent= getIntent();

        storeid= intent.getStringExtra("storeid");

        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

        textView_namee.setText(sharedPrefrenceUtil.getEmployeeCode());

        employee_codee.setText(sharedPrefrenceUtil.getName());

        imageView_backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProductList.this,StoreListActivity.class);
                startActivity(i);
                finish();


            }

        });


        getProductList();
        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveDataToServer();

            }
        });

    }

    public  void SaveDataToServer()

    {

        makeList();
        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

        if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {

            /* if (!lat.equals("0.0") && !longi.equals("0.0")) {*/
            showLoadingDialog(this);
            final String err = "    Successfully Data Save to Server..";

            PostStoreMaterialList loginRequestt = new PostStoreMaterialList(quantitiList,sharedPrefrenceUtil.getName(),mateialList,storeid);

            //            Toast.makeText(getApplicationContext(),""+quantitiList,Toast.LENGTH_LONG).show();
  //          Toast.makeText(getApplicationContext(),""+mateialList,Toast.LENGTH_LONG).show();

            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.dopostproductlist(loginRequestt).enqueue(new Callback<PostProducrListResponse>() {


                @Override
                public void onResponse(Call<PostProducrListResponse> call, Response<PostProducrListResponse> response) {

                    hideLoading();

                    if (response.body() != null) {
                        Boolean value = response.body().getStatus();
                        if (value.equals(true)) {

                            showMessage("Successfully Data Send To Server");

                            //everthing is ok
                            // Toast.makeText(getApplicationContext(),userStoreLists.get(1).getStoreName(),Toast.LENGTH_LONG).show();
                        }

                        else

                            {

                                showMessage("Successfully Data Save to Server..");//showTaost(msg);

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully Data Save to Server..", Toast.LENGTH_LONG).show();
                    }
                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<PostProducrListResponse> call, Throwable t) {
                    hideLoading();
                    showMessage(err);
                }
            });

               /* } else

                showMessage("Please Wait While we are Fetching your Location and Try Again!!");*/
        }

        else

            showMessage(getString(R.string.no_internet));

    }

    public void getProductList()
    {
        if (NetworkUtility.isNetworkAvailable(getApplicationContext())) {
            /* if (!lat.equals("0.0") && !longi.equals("0.0")) {*/
            showLoadingDialog(this);
            final String err = "Something went wrong please try again";
            SharedPrefrenceUtil sharedPrefrenceUtil= new SharedPrefrenceUtil(getApplicationContext());
            ItemListRequest loginRequestt = new ItemListRequest(sharedPrefrenceUtil.getName(),storeid);
            ApiInterface mapiClinet = RetrofitClientInstance.getApiClient();
            mapiClinet.doitemList(loginRequestt).enqueue(new Callback<ItemListResponse>() {

                @Override
                public void onResponse(Call<ItemListResponse> call, Response<ItemListResponse> response) {

                    hideLoading();

                    if (response.body() != null) {

                        Boolean value = response.body().getStatus();
                        if (value.equals(true)) {
                            //everthing is ok
                            //showMessage("uplaoded successfully");

                            ItemListResponse storeListResponse = response.body();
                            List<StoreMaterialList> userStoreLists= storeListResponse.getStoreMaterialList();
                           // Toast.makeText(getApplicationContext(),userStoreLists.get(1).getStoreName(),Toast.LENGTH_LONG).show();
                            SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
                           if(!sharedPrefrenceUtil.getQuantity().isEmpty()){
                               try
                               {
                                   JSONObject jsonObject = new JSONObject(sharedPrefrenceUtil.getQuantity());
                                   Iterator<String> iterator = jsonObject.keys();

                                   while(iterator.hasNext()){

                                       String key = iterator.next();
                                       String value1 = jsonObject.getString(key);
                                       adapterMap.put(key,value1);

                                   }

                               }

                               catch (JSONException e) {
                                   e.printStackTrace();

                               }

                           }

                            CustomListAdapter customAdpter = new CustomListAdapter(ProductList.this,userStoreLists,adapterMap);
                            listview.setAdapter(customAdpter);

                        }

                        else
                            {

                                showMessage("Not uplaodedddddd");
                            //showTaost(msg);
                        }

                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong....Please Try Again", Toast.LENGTH_LONG).show();
                    }
                    //  showTaost(msg);
                }

                @Override
                public void onFailure(Call<ItemListResponse> call, Throwable t) {
                    hideLoading();
                    showMessage(err);
                }
            });
               /* } else

                showMessage("Please Wait While we are Fetching your Location and Try Again!!");*/
        }

        else
            showMessage(getString(R.string.no_internet));


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

    @Override
    protected void onPause() {
        super.onPause();

        JSONObject jsonObject =new JSONObject();

        for(String key : adapterMap.keySet()) {

            try {

                jsonObject.put(key,adapterMap.get(key));
    }

       catch (JSONException e) {
        e.printStackTrace();

            }

        }

        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());
        sharedPrefrenceUtil.setQuantity(jsonObject.toString());
    }

    ArrayList<String> mateialList,quantitiList;

    void makeList(){

        mateialList = new ArrayList<>();
       quantitiList = new ArrayList<>();

        for(String key : adapterMap.keySet()) {
            mateialList.add(key);
            quantitiList.add(adapterMap.get(key));
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductList.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();

                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }
}
