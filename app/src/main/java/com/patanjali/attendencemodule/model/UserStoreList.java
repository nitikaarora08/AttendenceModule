
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class UserStoreList {

    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("storeId")
    private String mStoreId;
    @SerializedName("store_location")
    private String mStoreLocation;
    @SerializedName("store_name")
    private String mStoreName;

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

    public String getStoreLocation() {
        return mStoreLocation;
    }

    public void setStoreLocation(String storeLocation) {
        mStoreLocation = storeLocation;
    }

    public String getStoreName() {
        return mStoreName;
    }

    public void setStoreName(String storeName) {
        mStoreName = storeName;
    }

}
