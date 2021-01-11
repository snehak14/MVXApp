package com.mvxapp.profile.listener;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.profile.model.ProfileResponseData;

public interface ProfileContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestUserDetail(String userId);

        void handleEvents(MessageEvent messageEvent);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the fetchingdata and onResponseFailure is fetched from the GetProfileIntractorImpl class
     **/
    interface ProfileView {
        void hideProgress();

        void setUserDetail(ProfileResponseData profileData);

        void onResponseFailure(@Nullable Throwable throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetProfileIntractor {

        interface OnFinishedListener {
            void onFinished(ProfileResponseData profileData);

            void onFailure(@Nullable Throwable throwable);
        }

        void getUserDetail(OnFinishedListener onFinishedListener, Context c, String userId);
    }
}
