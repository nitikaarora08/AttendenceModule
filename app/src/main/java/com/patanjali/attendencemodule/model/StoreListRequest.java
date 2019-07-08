
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class StoreListRequest {

    @SerializedName("emp_code")
    private String mEmpCode;

    public StoreListRequest(String mEmpCode) {
        this.mEmpCode = mEmpCode;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

}
