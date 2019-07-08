
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class CheckOutRequest {

    @SerializedName("checkout_date")
    private String mCheckoutDate;
    @SerializedName("checkout_lat")
    private String mCheckoutLat;
    @SerializedName("checkout_long")
    private String mCheckoutLong;
    @SerializedName("emp_code")
    private String mEmpCode;

    public CheckOutRequest(String mCheckoutDate, String mCheckoutLat, String mCheckoutLong, String mEmpCode) {
        this.mCheckoutDate = mCheckoutDate;
        this.mCheckoutLat = mCheckoutLat;
        this.mCheckoutLong = mCheckoutLong;
        this.mEmpCode = mEmpCode;
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
