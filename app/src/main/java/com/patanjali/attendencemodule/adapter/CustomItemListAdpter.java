package com.patanjali.attendencemodule.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.activity.ProductList;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.PostProducrListResponse;
import com.patanjali.attendencemodule.model.PostProductListRequest;
import com.patanjali.attendencemodule.model.StoreMaterialList;
import com.patanjali.attendencemodule.network.ApiInterface;
import com.patanjali.attendencemodule.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;
import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;


/**
 * Created by Lenovo on 15-10-2017.
 */

public class CustomItemListAdpter extends Adapter<CustomItemListAdpter.MyViewHolder> {


    List<StoreMaterialList> arr=new ArrayList<>();
    List<String> product=new ArrayList<String>();
    List<String> materiallist=new ArrayList<>();
    ProgressDialog mprogressDialog;
    String itemname,storeid,materialid;
    ProductList context;
    String item_quantity="";
    HashMap<String,String> adapterMap;

    public CustomItemListAdpter(ProductList context, List<StoreMaterialList> arr,HashMap<String,String> adapterMap) {
        this.context = context;
        this.arr = arr;
        this.adapterMap = adapterMap;

    }

    public class MyViewHolder extends ViewHolder {
    public TextView items,item_mbq;

    public Button img_save;
    public EditText itemquantity;

    public MyViewHolder(View view) {
        super(view);
        this.items = (TextView) view.findViewById(R.id.items);
        this.item_mbq=(TextView) view.findViewById(R.id.item_mbq);
        this.itemquantity=(EditText) view.findViewById(R.id.itemquantity);

    }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_items, parent, false);

        MyViewHolder mh = new MyViewHolder(itemView);
        return mh;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

        if (sharedPrefrenceUtil.getQuantity()!=null)
        {
            //holder.itemquantity.setText(sharedPrefrenceUtil.getQuantity());
        }

        itemname=arr.get(position).getMaterialName();
        final String itemmbq=arr.get(position).getStoreItemMbq();
        storeid= arr.get(position).getStoreId();
        materialid=arr.get(position).getMaterialId();
        holder.items.setText(itemname);
        holder.item_mbq.setText(itemmbq);

        if(adapterMap.get(materialid) != null) {
    //holder.itemquantity.setText(adapterMap.get(materialid));

        }

        holder.itemquantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            //    adapterMap.put(arr.get(position).getMaterialId(),holder.itemquantity.getText().toString());
              //  Toast.makeText(getApplicationContext(),""+adapterMap,Toast.LENGTH_LONG).show();
                Log.d("key",""+adapterMap);

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

    }

    public  void hideLoading() {

        if (mprogressDialog != null && mprogressDialog.isShowing()) {

            mprogressDialog.cancel();

        }
    }
    public  void showLoadingDialog() {
        mprogressDialog = new ProgressDialog(getApplicationContext());
        mprogressDialog.show();
        if (mprogressDialog.getWindow() != null) {
            mprogressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mprogressDialog.setContentView(R.layout.progress_layout);
        mprogressDialog.setIndeterminate(true);
        mprogressDialog.setCancelable(false);

        mprogressDialog.setCanceledOnTouchOutside(false);

    }

    public void showMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return (null != arr ? arr.size() : 0);
    }
}
