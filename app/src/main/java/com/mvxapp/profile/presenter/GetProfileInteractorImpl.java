package com.mvxapp.profile.presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mvxapp.common.EndPointUtils;
import com.mvxapp.profile.listener.ProfileContract;
import com.mvxapp.profile.model.ProfileResponseData;
import com.mvxapp.remote.APIInterface;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GetProfileInteractorImpl implements ProfileContract.GetProfileIntractor {
    private static final String LOG_TAG = GetProfileInteractorImpl.class.getSimpleName();
    private OnFinishedListener mOnFinishedListener;
    private String mUserId;

    @Override
    public void getUserDetail(OnFinishedListener onFinishedListener, Context c, String userId) {
        mOnFinishedListener = onFinishedListener;
        mUserId = userId;

        getObservable().subscribe(getObserver());
    }

    private Observable<ProfileResponseData> getObservable() {
        APIInterface mApiInterface = EndPointUtils.getUserInfoInterface();
        return mApiInterface
                .profileData(mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<ProfileResponseData> getObserver() {
        return new DisposableObserver<ProfileResponseData>() {

            @Override
            public void onNext(@NonNull ProfileResponseData profileResponse) {
                Log.d(LOG_TAG, "OnNext" + profileResponse);
                mOnFinishedListener.onFinished(profileResponse);
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
