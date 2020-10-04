package com.mvxapp.home;

import android.os.Bundle;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.utils.MVXUtils;

public class HomeActivity extends BaseActivity {
    @Override
    protected void networkState(boolean netAvailable) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }
}
