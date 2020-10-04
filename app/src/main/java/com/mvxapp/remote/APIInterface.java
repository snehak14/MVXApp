package com.mvxapp.remote;

import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @POST("users")
    Observable<RegisterResponseData> registerUser(@Body RequestBody body);

    @POST("login")
    Observable<LoginResponseData> loginUser(@Body RequestBody body);

    @POST("verifyOtp")
    Observable<LoginResponseData> otpVerify(@Body RequestBody body);

    @GET("retryOtp/{mobile}")
    Observable<ResendOtpResponseData> resendOtp(@Path(value = "mobile") Long mobileNumber);
}
