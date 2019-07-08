
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class OTPVerifyResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("Outh_token")
    private String mOuthToken;
    @SerializedName("status")
    private Boolean mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getOuthToken() {
        return mOuthToken;
    }

    public void setOuthToken(String outhToken) {
        mOuthToken = outhToken;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
