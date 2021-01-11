package com.mvxapp.remote;

import com.mvxapp.login.model.ForgotPasswordResponseData;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;
import com.mvxapp.profile.model.ProfileResponseData;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("forgotPwdOtp/{email}")
    Observable<ForgotPasswordResponseData> forgotPasswordOtp(@Path(value = "email") String emailId);

    @PUT("resetForgotPwd")
    Observable<LoginResponseData> resetPassword(@Body RequestBody body);

    @GET("users/{userId}")
    Observable<ProfileResponseData> profileData(@Path(value = "userId") String userId);

    @PUT("/loginSocial")
    Observable<RegisterResponseData> loginUserSocial(@Body RequestBody body);

}
