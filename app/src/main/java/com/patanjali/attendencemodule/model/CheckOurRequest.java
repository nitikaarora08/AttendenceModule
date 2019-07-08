
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CheckOurRequest {

    @SerializedName("checkout_address")
    private String mCheckoutAddress;
    @SerializedName("checkout_date")
    private String mCheckoutDate;
    @SerializedName("checkout_lat")
    private String mCheckoutLat;
    @SerializedName("checkout_long")
    private String mCheckoutLong;
    @SerializedName("emp_code")
    private String mEmpCode;

    public CheckOurRequest(String mCheckoutAddress, String mCheckoutDate, String mCheckoutLat, String mCheckoutLong, String mEmpCode) {
        this.mCheckoutAddress = mCheckoutAddress;
        this.mCheckoutDate = mCheckoutDate;
        this.mCheckoutLat = mCheckoutLat;
        this.mCheckoutLong = mCheckoutLong;
        this.mEmpCode = mEmpCode;
    }

    public String getCheckoutAddress() {
        return mCheckoutAddress;
    }

    public void setCheckoutAddress(String checkoutAddress) {
        mCheckoutAddress = checkoutAddress;
    }

    public String getCheckoutDate() {
        return mCheckoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        mCheckoutDate = checkoutDate;
    }

    public String getCheckoutLat() {
        return mCheckoutLat;
    }

    public void setCheckoutLat(String checkoutLat) {
        mCheckoutLat = checkoutLat;
    }

    public String getCheckoutLong() {
        return mCheckoutLong;
    }

    public void setCheckoutLong(String checkoutLong) {
        mCheckoutLong = checkoutLong;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

}
