package com.mvxapp.login.presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mvxapp.common.EndPointUtils;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;
import com.mvxapp.remote.APIInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class GetRegisterInteractorImpl implements RegisterContract.GetRegisterIntractor {
    private static final String LOG_TAG = GetRegisterInteractorImpl.class.getSimpleName();
    private OnFinishedListener mOnFinishedListener;
    private RequestBody mUserDetail;
    private RequestBody mUserOtp;

    @Override
    public void getRegister(OnFinishedListener onFinishedListener, Context c, RequestBody userDetail) {
        mOnFinishedListener = onFinishedListener;
        mUserDetail = userDetail;

        getObservable().subscribe(getObserver());
    }

    @Override
    public void OtpVerify(OnFinishedListener onFinishedListener, Context c, RequestBody userOtp) {
        mOnFinishedListener = onFinishedListener;
        mUserOtp = userOtp;

        getOtpObservable().subscribe(getOtpObserver());
    }

    @Override
    public void ResendOtp(OnFinishedListener onFinishedListener, Context c, Long mobileNumber) {
        mOnFinishedListener = onFinishedListener;

        getResendOtpObservable(mobileNumber).subscribe(getResendOtpObserver());
    }
    private Observable<RegisterResponseData> getObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .registerUser(mUserDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<RegisterResponseData> getObserver() {
        return new DisposableObserver<RegisterResponseData>() {

            @Override
            public void onNext(@NonNull RegisterResponseData registerResponse) {
                Log.d(LOG_TAG, "OnNext" + registerResponse.getData());
                if (registerResponse != null) {
                    mOnFinishedListener.onFinished(registerResponse);
                } else {
                    mOnFinishedListener.onFailure(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(LOG_TAG, "Error" + e);
                e.printStackTrace();
                mOnFinishedListener.onFailure(e);
            }

            @Override
            public void onComplete() {
                Log.d(LOG_TAG, "Completed");
            }
        };
    }

    private Observable<LoginResponseData> getOtpObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .otpVerify(mUserOtp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<LoginResponseData> getOtpObserver() {
        return new DisposableObserver<LoginResponseData>() {

            @Override
            public void onNext(@NonNull LoginResponseData otpResponse) {
                Log.d(LOG_TAG, "OnNext" + otpResponse);
                if (otpResponse != null) {
                    mOnFinishedListener.onFinishedOtp(otpResponse);
                } else {
                    mOnFinishedListener.onFailureOtp(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(LOG_TAG, "Error" + e);
                e.printStackTrace();
                mOnFinishedListener.onFailureOtp(e);
            }

            @Override
            public void onComplete() {
                Log.d(LOG_TAG, "Completed");
            }
        };
    }

    private Observable<ResendOtpResponseData> getResendOtpObservable(Long mobileNumber) {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .resendOtp(mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<ResendOtpResponseData> getResendOtpObserver() {
        return new DisposableObserver<ResendOtpResponseData>() {

            @Override
            public void onNext(@NonNull ResendOtpResponseData otpResponse) {
                Log.d(LOG_TAG, "OnNext" + otpResponse);
                if (otpResponse != null) {
                    mOnFinishedListener.onFinishedResendOtp(otpResponse);
                } else {
                    mOnFinishedListener.onFailureResendOtp(null);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(LOG_TAG, "Error" + e);
                e.printStackTrace();
                mOnFinishedListener.onFailureResendOtp(e);
            }

            @Override
            public void onComplete() {
                Log.d(LOG_TAG, "Completed");
            }
        };
    }
}
