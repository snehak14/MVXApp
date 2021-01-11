package com.mvxapp.login.listener;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.model.ForgotPasswordResponseData;
import com.mvxapp.login.model.LoginResponseData;

import okhttp3.RequestBody;

public interface ForgotPasswordContract {
    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestPasswordOTP(String emailId);

        void requestResetPassword(RequestBody resetPasswordDetail);

        void handleEvents(MessageEvent messageEvent);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the fetchingdata and onResponseFailure is fetched from the GetForgotPasswordIntractorImpl class
     **/
    interface ResetPasswordView {
        void hideProgress();

        void setPassword(ForgotPasswordResponseData forgotPasswordResponseData);

        void setResetPassword(LoginResponseData resetPasswordResponseData);

        void onResponsePasswordFailure(@Nullable Throwable throwable);

        void onResponseResetPasswordFailure(@Nullable Throwable throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetForgotPasswordIntractor {

        interface OnFinishedListener {
            void onPasswordFinished(ForgotPasswordResponseData forgotPasswordResponseData);

            void onResetPasswordFinished(LoginResponseData resetPasswordResponseData);

            void onPasswordFailure(@Nullable Throwable throwable);

            void onResetPasswordFailure(@Nullable Throwable throwable);
        }

        void getForgotPassOTP(OnFinishedListener onFinishedListener, Context c, String emailID);

        void getResetPassword(OnFinishedListener onFinishedListener, Context context, RequestBody resetPasswordDetail);
    }
}
