
package com.patanjali.attendencemodule.model;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class EmpDetail {

    @SerializedName("created_date")
    private String mCreatedDate;
    @SerializedName("emp_code")
    private String mEmpCode;
    @SerializedName("emp_email")
    private String mEmpEmail;
    @SerializedName("emp_id")
    private String mEmpId;
    @SerializedName("emp_level")
    private String mEmpLevel;
    @SerializedName("emp_mobileno")
    private String mEmpMobileno;
    @SerializedName("emp_name")
    private String mEmpName;
    @SerializedName("emp_status")
    private String mEmpStatus;
    @SerializedName("emp_suprvisior_code")
    private String mEmpSuprvisiorCode;

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getEmpCode() {
        return mEmpCode;
    }

    public void setEmpCode(String empCode) {
        mEmpCode = empCode;
    }

    public String getEmpEmail() {
        return mEmpEmail;
    }

    public void setEmpEmail(String empEmail) {
        mEmpEmail = empEmail;
    }

    public String getEmpId() {
        return mEmpId;
    }

    public void setEmpId(String empId) {
        mEmpId = empId;
    }

    public String getEmpLevel() {
        return mEmpLevel;
    }

    public void setEmpLevel(String empLevel) {
        mEmpLevel = empLevel;
    }

    public String getEmpMobileno() {
        return mEmpMobileno;
    }

    public void setEmpMobileno(String empMobileno) {
        mEmpMobileno = empMobileno;
    }

    public String getEmpName() {
        return mEmpName;
    }

    public void setEmpName(String empName) {
        mEmpName = empName;
    }

    public String getEmpStatus() {
        return mEmpStatus;
    }

    public void setEmpStatus(String empStatus) {
        mEmpStatus = empStatus;
    }

    public String getEmpSuprvisiorCode() {
        return mEmpSuprvisiorCode;
    }

    public void setEmpSuprvisiorCode(String empSuprvisiorCode) {
        mEmpSuprvisiorCode = empSuprvisiorCode;
    }

}
