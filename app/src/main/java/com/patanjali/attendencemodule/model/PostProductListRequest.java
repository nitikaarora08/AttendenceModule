
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PostProductListRequest {

    @SerializedName("avail_qty")
    private String mAvailQty;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("material_id")
    private String mMaterialId;
    @SerializedName("store_id")
    private String mStoreId;

    public PostProductListRequest(String mAvailQty, String mEmpCode, String mMaterialId, String mStoreId) {
        this.mAvailQty = mAvailQty;
        this.mEmpCode = mEmpCode;
        this.mMaterialId = mMaterialId;
        this.mStoreId = mStoreId;
    }

    public String getAvailQty() {
        return mAvailQty;
    }

    public void setAvailQty(String availQty) {
        mAvailQty = availQty;
    }

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

    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(String storeId) {
        mStoreId = storeId;
    }

}
