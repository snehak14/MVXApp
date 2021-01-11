package com.mvxapp.profile.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.profile.listener.ProfileContract;
import com.mvxapp.profile.model.ProfileResponseData;

public class ProfilePresenterImpl implements ProfileContract.presenter, ProfileContract.GetProfileIntractor.OnFinishedListener {

    private ProfileContract.ProfileView mProfileView;
    private ProfileContract.GetProfileIntractor mGetProfileIntractor;
    private Context mContext;

    public ProfilePresenterImpl(ProfileContract.ProfileView profileView, ProfileContract.GetProfileIntractor getprofileIntractor, Context context) {
        this.mProfileView = profileView;
        this.mGetProfileIntractor = getprofileIntractor;
        mContext = context;
    }

    @Override
    public void onDestroy() {
        mProfileView = null;
    }

    @Override
    public void requestUserDetail(String userId) {
        mGetProfileIntractor.getUserDetail(this, mContext, userId);
    }

    @Override
    public void handleEvents(MessageEvent messageEvent) {

    }

    @Override
    public void onFinished(ProfileResponseData profileData) {
        mProfileView.setUserDetail(profileData);
    }

    @Override
    public void onFailure(@Nullable Throwable throwable) {
        if (throwable != null){
            mProfileView.onResponseFailure(throwable);
        }
    }
}
