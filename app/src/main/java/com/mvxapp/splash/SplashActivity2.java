package com.mvxapp.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.home.HomeActivity;
import com.mvxapp.login.view.LoginActivity;
import com.mvxapp.utils.MVXUtils;

public class SplashActivity2 extends BaseActivity {
    SharedPreferences prefs;
    String loginToken;

    @Override
    protected void networkState(boolean netAvailable) {
        if (!netAvailable){
            Toast.makeText(this, "Internet not available. Please check and re-try", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash2);

        MVXUtils.hideStatusBar(this);

        prefs = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        loginToken = prefs.getString("loginToken", null);

        initiateApp();
    }

    private void initiateApp() {
        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs != null && loginToken != null){
                    Intent intent = new Intent(SplashActivity2.this, HomeActivity.class);
                    SplashActivity2.this.startActivity(intent);
                    SplashActivity2.this.finish();
                } else {
                    Intent intent = new Intent(SplashActivity2.this, LoginActivity.class);
                    SplashActivity2.this.startActivity(intent);
                    SplashActivity2.this.finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
