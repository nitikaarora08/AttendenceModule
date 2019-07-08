
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class MultipleLatLonRequest {

    @SerializedName("address")
    private List<String> mAddress;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("emp_latitude")
    private List<String> mEmpLatitude;
    @SerializedName("emp_longitude")
    private List<String> mEmpLongitude;
    @SerializedName("entry_date")
    private String mEntryDate;
    @SerializedName("entry_datetime")
    private List<String> mEntryDatetime;

    public MultipleLatLonRequest(List<String> mAddress, String mEmpCode, List<String> mEmpLatitude, List<String> mEmpLongitude, String mEntryDate, List<String> mEntryDatetime) {
        this.mAddress = mAddress;
        this.mEmpCode = mEmpCode;
        this.mEmpLatitude = mEmpLatitude;
        this.mEmpLongitude = mEmpLongitude;
        this.mEntryDate = mEntryDate;
        this.mEntryDatetime = mEntryDatetime;
    }

    public List<String> getAddress() {
        return mAddress;
    }

    public void setAddress(List<String> address) {
        mAddress = address;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public List<String> getEmpLatitude() {
        return mEmpLatitude;
    }

    public void setEmpLatitude(List<String> empLatitude) {
        mEmpLatitude = empLatitude;
    }

    public List<String> getEmpLongitude() {
        return mEmpLongitude;
    }

    public void setEmpLongitude(List<String> empLongitude) {
        mEmpLongitude = empLongitude;
    }

    public String getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(String entryDate) {
        mEntryDate = entryDate;
    }

    public List<String> getEntryDatetime() {
        return mEntryDatetime;
    }

    public void setEntryDatetime(List<String> entryDatetime) {
        mEntryDatetime = entryDatetime;
    }

}
