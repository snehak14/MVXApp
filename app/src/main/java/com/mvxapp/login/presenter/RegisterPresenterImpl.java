package com.mvxapp.login.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;

import okhttp3.RequestBody;

public class RegisterPresenterImpl implements RegisterContract.presenter, RegisterContract.GetRegisterIntractor.OnFinishedListener {

    private RegisterContract.RegisterView mRegisterView;
    private RegisterContract.GetRegisterIntractor mGetRegisterIntractor;
    private Context mContext;

    public RegisterPresenterImpl(RegisterContract.RegisterView registerView, RegisterContract.GetRegisterIntractor getregisterIntractor, Context context) {
        this.mRegisterView = registerView;
        this.mGetRegisterIntractor = getregisterIntractor;
        mContext = context;
    }

    @Override
    public void onDestroy() {
        mRegisterView = null;
    }

    @Override
    public void requestRegister(RequestBody registerDetail) {
        mGetRegisterIntractor.getRegister(this, mContext, registerDetail);
    }

    @Override
    public void handleEvents(MessageEvent messageEvent) {

    }

    @Override
    public void requestOtp(RequestBody userOtp) {
        mGetRegisterIntractor.OtpVerify(this, mContext, userOtp);
    }

    @Override
    public void resendOtp(Long mobile) {
        mGetRegisterIntractor.ResendOtp(this, mContext, mobile);
    }

    @Override
    public void socialLogin(RequestBody userDetail) {
        mGetRegisterIntractor.getRegister(this, mContext, userDetail);
    }

    @Override
    public void onFinished(RegisterResponseData userData) {
        mRegisterView.setRegister(userData);
    }

    @Override
    public void onFailure(@Nullable Throwable throwable) {
        if (throwable != null) {
            mRegisterView.onResponseFailure(throwable);
        }
    }

    @Override
    public void onFinishedOtp(LoginResponseData loginData) {
        mRegisterView.otpVerify(loginData);
    }

    @Override
    public void onFailureOtp(@Nullable Throwable throwable) {
        if (throwable != null){
            mRegisterView.onOtpFailure(throwable);
        }
    }

    @Override
    public void onFinishedResendOtp(ResendOtpResponseData resendOtpResponseData) {
        mRegisterView.resendOtp(resendOtpResponseData);
    }

    @Override
    public void onFailureResendOtp(@Nullable Throwable throwable) {
        if (throwable != null){
            mRegisterView.onResendFailure(throwable);
        }
    }

    @Override
    public void onSocialFinished(RegisterResponseData registerResponseData) {
        mRegisterView.setRegister(registerResponseData);
    }

    @Override
    public void onSocialFailure(@Nullable Throwable throwable) {
        if (throwable != null) {
            mRegisterView.onSocialResponseFailure(throwable);
        }
    }
}
