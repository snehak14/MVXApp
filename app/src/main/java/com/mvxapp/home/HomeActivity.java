package com.mvxapp.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.login.view.LoginActivity;
import com.mvxapp.profile.view.ProfileActivity;
import com.mvxapp.utils.MVXUtils;
import com.unity3d.player.UnityPlayerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    SharedPreferences prefs;

    @Override
    protected void networkState(boolean netAvailable) {
        if (!netAvailable) {
            Toast.makeText(this, "Internet not available. Please check and re-try", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MVXUtils.hideStatusBar(this);

        prefs = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String loginToken = prefs.getString("loginToken", "");

    }

    @OnClick({R.id.expo_btn})
    public void callExpoApp() {
        Intent myIntent = new Intent(this, UnityPlayerActivity.class);
        // Needed to set component to remove explicit activity entry in application's manifest
        final ComponentName component = new ComponentName(this, UnityPlayerActivity.class);
        myIntent.setComponent(component);
        startActivity(myIntent);
    }

    @OnClick({R.id.about_btn})
    public void callAboutScreen() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @OnClick({R.id.setting_btn})
    public void callSetting() {
        showSettingsPopup(HomeActivity.this.findViewById(R.id.setting_btn));
    }

    public void showSettingsPopup(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_settings, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.END, 0, 0);

        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
// add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);

        ImageView crossBtn = popupView.findViewById(R.id.crossImage);
        crossBtn.setOnClickListener(view1 -> popupWindow.dismiss());

        TextView profileText = popupView.findViewById(R.id.profileTxt);
        profileText.setOnClickListener(view12 -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        TextView feedbackText = popupView.findViewById(R.id.feedbackTxt);
        feedbackText.setOnClickListener(view13 -> {
            final String appPackageName = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        TextView logOutText = popupView.findViewById(R.id.logoutTxt);
        logOutText.setOnClickListener(view14 -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("loginToken", null);
            editor.apply();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Are you sure you want to exit application? Click Confirm to exit the app.")
                .setCancelable(false)
                .setPositiveButton("Confirm",
                        (dialog, id) -> {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        })

                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
