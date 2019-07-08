
package com.patanjali.attendencemodule.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class PostStoreMaterialList {

    @SerializedName("avail_qty")
    private List<String> mAvailQty;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("material_id")
    private List<String> mMaterialId;
    @SerializedName("store_id")
    private String mStoreId;

    public PostStoreMaterialList(List<String> mAvailQty, String mEmpCode, List<String> mMaterialId, String mStoreId) {
        this.mAvailQty = mAvailQty;
        this.mEmpCode = mEmpCode;
        this.mMaterialId = mMaterialId;
        this.mStoreId = mStoreId;
    }

    public List<String> getAvailQty() {
        return mAvailQty;
    }

    public void setAvailQty(List<String> availQty) {
        mAvailQty = availQty;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public List<String> getMaterialId() {
        return mMaterialId;
    }

    public void setMaterialId(List<String> materialId) {
        mMaterialId = materialId;
    }

    public String getStoreId() {
        return mStoreId;
    }

    public void setStoreId(String storeId) {
        mStoreId = storeId;
    }

}
