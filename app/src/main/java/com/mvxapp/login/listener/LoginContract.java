package com.mvxapp.login.listener;

import android.content.Context;

import androidx.annotation.Nullable;

import com.mvxapp.common.MessageEvent;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;

import okhttp3.RequestBody;

public interface LoginContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestLogin(RequestBody loginDetail);

        void handleEvents(MessageEvent messageEvent);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the fetchingdata and onResponseFailure is fetched from the GetRegisterIntractorImpl class
     **/
    interface LoginView {
        void hideProgress();

        void setLogin(LoginResponseData loginData);

        void onResponseFailure(@Nullable Throwable throwable);
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetLoginIntractor {

        interface OnFinishedListener {
            void onFinished(LoginResponseData loginData);

            void onFailure(@Nullable Throwable throwable);
        }

        void getLogin(OnFinishedListener onFinishedListener, Context c, RequestBody loginDetail);
    }
}
