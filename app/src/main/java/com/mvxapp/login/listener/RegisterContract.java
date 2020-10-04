package com.mvxapp.login.listener;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;

import okhttp3.RequestBody;

public interface RegisterContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestRegister(RequestBody userDetail);

        void handleEvents(MessageEvent messageEvent);

        void requestOtp(RequestBody userOtp);

        void resendOtp(Long mobile);
    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the fetchingdata and onResponseFailure is fetched from the GetRegisterIntractorImpl class
     **/
    interface RegisterView {
        void hideProgress();

        void setRegister(RegisterResponseData registerData);

        void otpVerify(LoginResponseData loginData);

        void onResponseFailure(@Nullable Throwable throwable);

        void onOtpFailure(@Nullable Throwable throwable);

        void resendOtp(ResendOtpResponseData resendOtpResponseData);

        void onResendFailure(@Nullable Throwable throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetRegisterIntractor {

        interface OnFinishedListener {
            void onFinished(RegisterResponseData registerData);

            void onFailure(@Nullable Throwable throwable);

            void onFinishedOtp(LoginResponseData loginData);

            void onFailureOtp(@Nullable Throwable throwable);

            void onFinishedResendOtp(ResendOtpResponseData resendOtpResponseData);

            void onFailureResendOtp(@Nullable Throwable throwable);
        }

        void getRegister(OnFinishedListener onFinishedListener, Context c, RequestBody userDetail);

        void OtpVerify(OnFinishedListener onFinishedListener, Context c, RequestBody userOtp);

        void ResendOtp(OnFinishedListener onFinishedListener, Context c, Long mobileNumber);
    }
}
