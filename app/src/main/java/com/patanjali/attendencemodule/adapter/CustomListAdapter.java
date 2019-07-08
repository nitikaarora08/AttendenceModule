package com.patanjali.attendencemodule.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.activity.ProductList;
import com.patanjali.attendencemodule.database.SharedPrefrenceUtil;
import com.patanjali.attendencemodule.model.StoreMaterialList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class CustomListAdapter extends ArrayAdapter<StoreMaterialList> {

    List<StoreMaterialList> arr=new ArrayList<>();
    List<String> product=new ArrayList<String>();
    List<String> materiallist=new ArrayList<>();
    ProgressDialog mprogressDialog;
    String itemname,storeid,materialid;
    EditText itemquantity;
    ProductList context;
    String item_quantity="";
    HashMap<String,String> adapterMap;


    public CustomListAdapter(ProductList context, List<StoreMaterialList> arr, HashMap<String,String> adapterMap) {

        super(context, R.layout.store_list, arr);
        this.context = context;
        this.arr = arr;
        this.adapterMap = adapterMap;


    }

    public View getView(final int position, View view, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View vieww=inflater.inflate(R.layout.layout_items, null,true);
        TextView items,item_mbq;
        Button img_save;
        items = (TextView) vieww.findViewById(R.id.items);
        item_mbq=(TextView) vieww.findViewById(R.id.item_mbq);
        itemquantity=(EditText) vieww.findViewById(R.id.itemquantity);


        SharedPrefrenceUtil sharedPrefrenceUtil = new SharedPrefrenceUtil(getApplicationContext());

        if (sharedPrefrenceUtil.getQuantity()!=null)
        {
            //holder.itemquantity.setText(sharedPrefrenceUtil.getQuantity());
        }

        itemname=arr.get(position).getMaterialName();
        final String itemmbq=arr.get(position).getStoreItemMbq();
        storeid= arr.get(position).getStoreId();
        materialid=arr.get(position).getMaterialId();
        items.setText(itemname);
        item_mbq.setText(itemmbq);

        if(adapterMap.get(materialid) != null) {

              itemquantity.setText(adapterMap.get(materialid));

        }

        itemquantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty())

                {
                    adapterMap.remove(arr.get(position).getMaterialId());

                }

                    else
                        {

                    adapterMap.put(arr.get(position).getMaterialId(), s.toString());

                }

                //Toast.makeText(getApplicationContext(),""+s.toString(),Toast.LENGTH_LONG).show();
                Log.d("key",""+adapterMap);

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

        });



        return vieww;

    };


}
