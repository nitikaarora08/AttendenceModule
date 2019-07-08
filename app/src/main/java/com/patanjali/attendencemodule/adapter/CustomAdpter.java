package com.patanjali.attendencemodule.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.patanjali.attendencemodule.R;
import com.patanjali.attendencemodule.activity.ProductList;
import com.patanjali.attendencemodule.activity.StoreListActivity;
import com.patanjali.attendencemodule.model.UserStoreList;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;


/**
 * Created by Lenovo on 15-10-2017.
 */

public class CustomAdpter extends Adapter<CustomAdpter.MyViewHolder> {

    Activity context;

    List<UserStoreList> arr=new ArrayList<>();

    public CustomAdpter(Activity context, List<UserStoreList> arr) {
        this.context = context;
        this.arr = arr;

    }

    public class MyViewHolder extends ViewHolder {
    public TextView storename;

    RelativeLayout relativeLayout;

    public MyViewHolder(View view) {
        super(view);
        this.storename = (TextView) view.findViewById(R.id.storename);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);


    }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_products, parent, false);

        MyViewHolder mh = new MyViewHolder(itemView);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String machinename=arr.get(position).getStoreName();
        holder.storename.setText(machinename);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,ProductList.class);
                i.putExtra("storeid",arr.get(position).getStoreId());
                /* i.putExtra("Name",arr.get(position).getRetailerName());
                i.putExtra("Beat",arr.get(position).getRetailerMobileno());
                i.putExtra("ShortNmae",arr.get(position).getRetailerAnniversary());
                i.putExtra("District",arr.get(position).getRetailerDistrict());
                i.putExtra("WeeklyOff",arr.get(position).getRetailerWeeklyoff());
                i.putExtra("Pincode",arr.get(position).getPinCode());*/

                context.startActivity(i);
                context.finish();

               //  Toast.makeText(context,holder.storename.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }

       /* holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    if(!context.machinedata.contains(arr.get(position).getMachinaryId())) {
                        context.machinedata.add(arr.get(position).getMachinaryId());

                    }

                    //context.spinnerValueMap.put(position,arr.get(position));
                }

                else {
                    // context.spinnerValueMap.remove(position);
                    context.machinedata.remove(arr.get(position).getMachinaryId());
                }

            }
        });*/


    @Override
    public int getItemCount() {
        return (null != arr ? arr.size() : 0);
    }

}
