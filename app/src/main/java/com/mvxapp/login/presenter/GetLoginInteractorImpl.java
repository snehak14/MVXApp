package com.mvxapp.login.presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mvxapp.common.EndPointUtils;
import com.mvxapp.login.listener.LoginContract;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.remote.APIInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class GetLoginInteractorImpl implements LoginContract.GetLoginIntractor {
    private static final String LOG_TAG = GetLoginInteractorImpl.class.getSimpleName();
    private OnFinishedListener mOnFinishedListener;
    private RequestBody mLoginDetail;

    @Override
    public void getLogin(OnFinishedListener onFinishedListener, Context c, RequestBody loginDetail) {
        mOnFinishedListener = onFinishedListener;
        mLoginDetail = loginDetail;

        getObservable().subscribe(getObserver());
    }

    private Observable<LoginResponseData> getObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .loginUser(mLoginDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<LoginResponseData> getObserver() {
        return new DisposableObserver<LoginResponseData>() {

            @Override
            public void onNext(@NonNull LoginResponseData loginResponse) {
                Log.d(LOG_TAG, "OnNext" + loginResponse);
                if (loginResponse != null) {
                    mOnFinishedListener.onFinished(loginResponse);
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
}
