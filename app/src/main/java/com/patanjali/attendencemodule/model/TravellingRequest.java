
package com.patanjali.attendencemodule.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TravellingRequest {

    @SerializedName("arrival_time")
    private String mArrivalTime;
    @SerializedName("arrival_town")
    private String mArrivalTown;
    @SerializedName("convence")
    private String mConvence;
    @SerializedName("depart_time")
    private String mDepartTime;
    @SerializedName("depart_town")
    private String mDepartTown;
    @SerializedName("distance")
    private String mDistance;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("entry_date")
    private String mEntryDate;
    @SerializedName("fare")
    private String mFare;
    @SerializedName("food_expense")
    private String mFoodExpense;
    @SerializedName("hotel_expense")
    private String mHotelExpense;
    @SerializedName("misc_expense")
    private String mMiscExpense;
    @SerializedName("mode_of_travel")
    private String mModeOfTravel;
    @SerializedName("total")
    private String mTotal;
    @SerializedName("travel_entry_date")
    private String mTravelEntryDate;

    public TravellingRequest(String mArrivalTime, String mArrivalTown, String mConvence, String mDepartTime, String mDepartTown, String mDistance, String mEmpCode, String mEntryDate, String mFare, String mFoodExpense, String mHotelExpense, String mMiscExpense, String mModeOfTravel, String mTotal, String mTravelEntryDate) {
        this.mArrivalTime = mArrivalTime;
        this.mArrivalTown = mArrivalTown;
        this.mConvence = mConvence;
        this.mDepartTime = mDepartTime;
        this.mDepartTown = mDepartTown;
        this.mDistance = mDistance;
        this.mEmpCode = mEmpCode;
        this.mEntryDate = mEntryDate;
        this.mFare = mFare;
        this.mFoodExpense = mFoodExpense;
        this.mHotelExpense = mHotelExpense;
        this.mMiscExpense = mMiscExpense;
        this.mModeOfTravel = mModeOfTravel;
        this.mTotal = mTotal;
        this.mTravelEntryDate = mTravelEntryDate;
    }

    public String getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        mArrivalTime = arrivalTime;
    }

    public String getArrivalTown() {
        return mArrivalTown;
    }

    public void setArrivalTown(String arrivalTown) {
        mArrivalTown = arrivalTown;
    }

    public String getConvence() {
        return mConvence;
    }

    public void setConvence(String convence) {
        mConvence = convence;
    }

    public String getDepartTime() {
        return mDepartTime;
    }

    public void setDepartTime(String departTime) {
        mDepartTime = departTime;
    }

    public String getDepartTown() {
        return mDepartTown;
    }

    public void setDepartTown(String departTown) {
        mDepartTown = departTown;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public String getEntryDate() {
        return mEntryDate;
    }

    public void setEntryDate(String entryDate) {
        mEntryDate = entryDate;
    }

    public String getFare() {
        return mFare;
    }

    public void setFare(String fare) {
        mFare = fare;
    }

    public String getFoodExpense() {
        return mFoodExpense;
    }

    public void setFoodExpense(String foodExpense) {
        mFoodExpense = foodExpense;
    }

    public String getHotelExpense() {
        return mHotelExpense;
    }

    public void setHotelExpense(String hotelExpense) {
        mHotelExpense = hotelExpense;
    }

    public String getMiscExpense() {
        return mMiscExpense;
    }

    public void setMiscExpense(String miscExpense) {
        mMiscExpense = miscExpense;
    }

    public String getModeOfTravel() {
        return mModeOfTravel;
    }

    public void setModeOfTravel(String modeOfTravel) {
        mModeOfTravel = modeOfTravel;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String total) {
        mTotal = total;
    }

    public String getTravelEntryDate() {
        return mTravelEntryDate;
    }

    public void setTravelEntryDate(String travelEntryDate) {
        mTravelEntryDate = travelEntryDate;
    }

}
