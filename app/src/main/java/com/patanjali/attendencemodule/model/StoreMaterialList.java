
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StoreMaterialList {

    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("material_id")
    private String mMaterialId;
    @SerializedName("material_name")
    private String mMaterialName;
    @SerializedName("storeId")
    private String mStoreId;
    @SerializedName("store_item_mbq")
    private String mStoreItemMbq;
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

    public String getMaterialId() {
        return mMaterialId;
    }

    public void setMaterialId(String materialId) {
        mMaterialId = materialId;
    }

    public String getMaterialName() {
        return mMaterialName;
    }

    public void setMaterialName(String materialName) {
        mMaterialName = materialName;
    }

    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(String storeId) {
        mStoreId = storeId;
    }

    public String getStoreItemMbq() {
        return mStoreItemMbq;
    }

    public void setStoreItemMbq(String storeItemMbq) {
        mStoreItemMbq = storeItemMbq;
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
