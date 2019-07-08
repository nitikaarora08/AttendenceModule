
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DataSammaryRequest {

    @SerializedName("day_summary")
    private String mDaySummary;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("entry_datetime")
    private String mEntryDatetime;

    public DataSammaryRequest(String mDaySummary, String mEmpCode, String mEntryDatetime) {
        this.mDaySummary = mDaySummary;
        this.mEmpCode = mEmpCode;
        this.mEntryDatetime = mEntryDatetime;
    }

    public String getDaySummary() {
        return mDaySummary;
    }

    public void setDaySummary(String daySummary) {
        mDaySummary = daySummary;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public String getEntryDatetime() {
        return mEntryDatetime;
    }

    public void setEntryDatetime(String entryDatetime) {
        mEntryDatetime = entryDatetime;
    }

}
