package com.mvxapp.login.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.listener.ForgotPasswordContract;
import com.mvxapp.login.model.ForgotPasswordResponseData;
import com.mvxapp.login.model.LoginResponseData;

import okhttp3.RequestBody;

public class ForgotPasswordPresenterImpl implements ForgotPasswordContract.presenter, ForgotPasswordContract.GetForgotPasswordIntractor.OnFinishedListener {

    private ForgotPasswordContract.ResetPasswordView mResetPasswordView;
    private ForgotPasswordContract.GetForgotPasswordIntractor mGetForgotPasswordIntractor;
    private Context mContext;

    public ForgotPasswordPresenterImpl(ForgotPasswordContract.ResetPasswordView resetPasswordView, ForgotPasswordContract.GetForgotPasswordIntractor getforgotPasswordIntractor, Context context) {
        this.mResetPasswordView = resetPasswordView;
        this.mGetForgotPasswordIntractor = getforgotPasswordIntractor;
        mContext = context;
    }

    @Override
    public void onDestroy() {
        mResetPasswordView = null;
    }

    @Override
    public void requestPasswordOTP(String emailId) {
        mGetForgotPasswordIntractor.getForgotPassOTP(this, mContext, emailId);
    }

    @Override
    public void requestResetPassword(RequestBody resetPasswordDetail) {
        mGetForgotPasswordIntractor.getResetPassword(this, mContext, resetPasswordDetail);
    }

    @Override
    public void handleEvents(MessageEvent messageEvent) {

    }

    @Override
    public void onPasswordFinished(ForgotPasswordResponseData forgotPasswordResponseData) {
        mResetPasswordView.setPassword(forgotPasswordResponseData);
    }

    @Override
    public void onResetPasswordFinished(LoginResponseData resetPasswordResponseData) {
        mResetPasswordView.setResetPassword(resetPasswordResponseData);
    }

    @Override
    public void onPasswordFailure(@Nullable Throwable throwable) {
        if (throwable != null) {
            mResetPasswordView.onResponsePasswordFailure(throwable);
        }
    }

    @Override
    public void onResetPasswordFailure(@Nullable Throwable throwable) {
        if (throwable != null) {
            mResetPasswordView.onResponseResetPasswordFailure(throwable);
        }
    }
}
