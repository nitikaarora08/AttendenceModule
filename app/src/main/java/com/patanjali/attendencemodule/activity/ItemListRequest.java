
package com.patanjali.attendencemodule.activity;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")


public class ItemListRequest {

    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("store_id")
    private String mStoreId;


    public ItemListRequest(String mEmpCode, String mStoreId) {
        this.mEmpCode = mEmpCode;
        this.mStoreId = mStoreId;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(String storeId) {
        mStoreId = storeId;
    }

}
