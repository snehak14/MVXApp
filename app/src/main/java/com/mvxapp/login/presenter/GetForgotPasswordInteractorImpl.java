package com.mvxapp.login.presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mvxapp.common.EndPointUtils;
import com.mvxapp.login.listener.ForgotPasswordContract;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.ForgotPasswordResponseData;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.remote.APIInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class GetForgotPasswordInteractorImpl implements ForgotPasswordContract.GetForgotPasswordIntractor {
    private static final String LOG_TAG = GetForgotPasswordInteractorImpl.class.getSimpleName();
    private OnFinishedListener mOnFinishedListener;
    private RequestBody mResetPasswordDetail;
    private String mEmailId;
    @Override
    public void getForgotPassOTP(OnFinishedListener onFinishedListener, Context c, String emailID) {
        mOnFinishedListener = onFinishedListener;
        mEmailId = emailID;

        getOtpObservable().subscribe(getOtpObserver());
    }

    @Override
    public void getResetPassword(OnFinishedListener onFinishedListener, Context context, RequestBody resetPasswordDetail) {
        mOnFinishedListener = onFinishedListener;
        mResetPasswordDetail = resetPasswordDetail;

        getObservable().subscribe(getObserver());
    }

    private Observable<ForgotPasswordResponseData> getOtpObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .forgotPasswordOtp(mEmailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<ForgotPasswordResponseData> getOtpObserver() {
        return new DisposableObserver<ForgotPasswordResponseData>() {

            @Override
            public void onNext(@NonNull ForgotPasswordResponseData otpResponse) {
                Log.d(LOG_TAG, "OnNext" + otpResponse.getUser());
                if (otpResponse != null) {
                    mOnFinishedListener.onPasswordFinished(otpResponse);
                } else {
                    mOnFinishedListener.onPasswordFailure(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(LOG_TAG, "Error" + e);
                e.printStackTrace();
                mOnFinishedListener.onPasswordFailure(e);
            }

            @Override
            public void onComplete() {
                Log.d(LOG_TAG, "Completed");
            }
        };
    }

    private Observable<LoginResponseData> getObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .resetPassword(mResetPasswordDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<LoginResponseData> getObserver() {
        return new DisposableObserver<LoginResponseData>() {

            @Override
            public void onNext(@NonNull LoginResponseData resetPasswordResponse) {
                Log.d(LOG_TAG, "OnNext" + resetPasswordResponse.getMessage());
                if (resetPasswordResponse != null) {
                    mOnFinishedListener.onResetPasswordFinished(resetPasswordResponse);
                } else {
                    mOnFinishedListener.onResetPasswordFailure(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(LOG_TAG, "Error" + e);
                e.printStackTrace();
                mOnFinishedListener.onResetPasswordFailure(e);
            }

            @Override
            public void onComplete() {
                Log.d(LOG_TAG, "Completed");
            }
        };
    }
}
