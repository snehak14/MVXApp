package com.mvxapp.login.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.listener.LoginContract;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;

import okhttp3.RequestBody;

public class LoginPresenterImpl implements LoginContract.presenter, LoginContract.GetLoginIntractor.OnFinishedListener {

    private LoginContract.LoginView mLoginView;
    private LoginContract.GetLoginIntractor mGetLoginIntractor;
    private Context mContext;

    public LoginPresenterImpl(LoginContract.LoginView loginView, LoginContract.GetLoginIntractor getloginIntractor, Context context) {
        this.mLoginView = loginView;
        this.mGetLoginIntractor = getloginIntractor;
        mContext = context;
    }


    @Override
    public void onDestroy() {
        mLoginView = null;
    }

    @Override
    public void requestLogin(RequestBody loginDetail) {
        mGetLoginIntractor.getLogin(this, mContext, loginDetail);
    }

    @Override
    public void handleEvents(MessageEvent messageEvent) {

    }

    @Override
    public void onFinished(LoginResponseData loginData) {
        mLoginView.setLogin(loginData);
    }

    @Override
    public void onFailure(@Nullable Throwable throwable) {
        if (throwable != null){
            mLoginView.onResponseFailure(throwable);
        }
    }
}
