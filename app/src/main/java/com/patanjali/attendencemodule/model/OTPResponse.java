
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class OTPResponse {

    @SerializedName("emp_detail")
    private List<EmpDetail> mEmpDetail;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("otp")
    private String mOtp;
    @SerializedName("status")
    private Boolean mStatus;

    public List<EmpDetail> getEmpDetail() {
        return mEmpDetail;
    }

    public void setEmpDetail(List<EmpDetail> empDetail) {
        mEmpDetail = empDetail;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getOtp() {
        return mOtp;
    }

    public void setOtp(String otp) {
        mOtp = otp;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
