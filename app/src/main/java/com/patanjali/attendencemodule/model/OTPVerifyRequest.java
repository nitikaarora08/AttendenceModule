
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class OTPVerifyRequest {

    @SerializedName("device_id")
    private String mDeviceId;
    @SerializedName("emp_code")
    private String mEmpCode;

    public OTPVerifyRequest(String mDeviceId, String mEmpCode) {
        this.mDeviceId = mDeviceId;
        this.mEmpCode = mEmpCode;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

}
