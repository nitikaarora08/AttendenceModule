
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpCheckinResponseModel {

    @SerializedName("file_path")
    private String mFilePath;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Boolean mStatus;

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}