package com.mvxapp.login.view;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.login.adapter.LoginPagerAdapter;
import com.mvxapp.utils.MVXUtils;

public class LoginActivity extends BaseActivity {

    private TabLayout mTabLayout;

    ViewPager mViewPager;

    @Override
    protected void networkState(boolean netAvailable) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        MVXUtils.hideStatusBar(this);

        initView();

    }

    private void initView() {
        mViewPager = findViewById(R.id.viewpager_login);
        setupViewPager(mViewPager);

        mTabLayout = findViewById(R.id.tabs_login);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener(mViewPager));
    }

    private void setupViewPager(ViewPager viewPager) {
        LoginPagerAdapter adapter = new LoginPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(new LoginFragment(), getResources().getString(R.string.login_title));
        adapter.addFragment(new SignupFragment(), getResources().getString(R.string.signup_title));
        viewPager.setAdapter(adapter);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() + 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        onTabSelectedListener(viewPager);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }
        };
    }
}
