package com.patanjali.attendencemodule.network;


import com.patanjali.attendencemodule.activity.ItemListRequest;
import com.patanjali.attendencemodule.model.CheckOurRequest;
import com.patanjali.attendencemodule.model.CheckOutRequest;
import com.patanjali.attendencemodule.model.CheckOutResponse;
import com.patanjali.attendencemodule.model.DataSammaryRequest;
import com.patanjali.attendencemodule.model.DataSammaryResponse;
import com.patanjali.attendencemodule.model.EmpCheckinResponseModel;
import com.patanjali.attendencemodule.model.ItemListResponse;
import com.patanjali.attendencemodule.model.MultipleLatLonRequest;
import com.patanjali.attendencemodule.model.MultipleLatLongResponse;
import com.patanjali.attendencemodule.model.OTPRequest;
import com.patanjali.attendencemodule.model.OTPResponse;
import com.patanjali.attendencemodule.model.OTPVerifyRequest;
import com.patanjali.attendencemodule.model.OTPVerifyResponse;
import com.patanjali.attendencemodule.model.PostProducrListResponse;
import com.patanjali.attendencemodule.model.PostProductListRequest;
import com.patanjali.attendencemodule.model.PostStoreMaterialList;
import com.patanjali.attendencemodule.model.StoreListRequest;
import com.patanjali.attendencemodule.model.StoreListResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    /*--------Image Upload  API---------------------------*/



    @Multipart
    @POST("Emp_checkin/emp_checkin")
    Call<EmpCheckinResponseModel> doEmpCheckinn(@Part("emp_code") RequestBody field1, @Part("type_of_visit") RequestBody field2,
                                                @Part("attandance_for") RequestBody field3, @Part("checkin_lat") RequestBody field4,
                                                @Part("checkin_long") RequestBody field5,
                                                @Part("checkin_time") RequestBody field6, @Part("checkin_date") RequestBody field7,
                                                @Part("remarks") RequestBody field8, @Part MultipartBody.Part file, @Part("checkin_address") RequestBody field9);


    @POST("Emp_login/login")
    Call<OTPResponse> doLogin(@Body OTPRequest request);

    @POST("Emp_checkin/employee_latlong")
    Call<MultipleLatLongResponse> docheckout(@Body MultipleLatLonRequest request);

    @POST("Emp_checkin/emp_checkout")
    Call<CheckOutResponse> docheckoutsingle(@Body CheckOurRequest request);

    @POST("StoreDetails/user_Storelist")
    Call<StoreListResponse> dostorelist(@Body StoreListRequest request);

    @POST("StoreDetails/user_Storemateriallist")
    Call<ItemListResponse> doitemList(@Body ItemListRequest request);

    @POST("StoreDetails/Allstoremateriallist")
    Call<PostProducrListResponse> dopostproductlist(@Body PostStoreMaterialList request);

    @POST("Emp_checkin/DaySummary")
    Call<DataSammaryResponse> dodatasammary(@Body DataSammaryRequest request);

    @POST("Emp_login/logindetail")
    Call<OTPVerifyResponse> verifyotp(@Body OTPVerifyRequest request);

}
