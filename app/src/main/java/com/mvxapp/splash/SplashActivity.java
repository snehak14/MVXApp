package com.mvxapp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.utils.MVXUtils;

public class SplashActivity extends BaseActivity {
    @Override
    protected void networkState(boolean netAvailable) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        MVXUtils.hideStatusBar(this);

        initiateApp();
    }

    private void initiateApp() {
        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SplashActivity2.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
