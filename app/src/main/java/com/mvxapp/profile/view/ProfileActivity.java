package com.mvxapp.profile.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.profile.listener.ProfileContract;
import com.mvxapp.profile.model.ProfileResponseData;
import com.mvxapp.profile.presenter.GetProfileInteractorImpl;
import com.mvxapp.profile.presenter.ProfilePresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity implements ProfileContract.ProfileView{

    @BindView(R.id.userName_Txt)
    TextView mUserNameTxt;

    @BindView(R.id.userEmail_Txt)
    TextView mUserEmailTxt;

    @BindView(R.id.userAge_Txt)
    TextView mUserAgeTxt;

    @BindView(R.id.userPhone_Txt)
    TextView mUserPhoneTxt;

    @BindView(R.id.userAddress_Txt)
    TextView mUserAddressTxt;

    @BindView(R.id.userType_Txt)
    TextView mUserTypeTxt;

    private Context mContext;

    SharedPreferences prefs;

    private ProgressDialog progressBar;

    @Override
    protected void networkState(boolean netAvailable) {
        if (!netAvailable) {
            Toast.makeText(this, "Internet not available. Please check and re-try", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        mContext = ProfileActivity.this;

        prefs = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String loginToken = prefs.getString("loginToken", "");
        String loginId = prefs.getString("userID", "");

        getProfileData(loginId);

    }

    private void getProfileData(String loginId) {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        ProfileContract.presenter profilePresenter = new ProfilePresenterImpl(this, new GetProfileInteractorImpl(), mContext);
        profilePresenter.requestUserDetail(loginId);
    }

    @Override
    public void hideProgress() {
        progressBar.dismiss();
        Toast.makeText(mContext, "Something went wrong. Unable to fetch User Detail. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserDetail(ProfileResponseData profileData) {
        progressBar.dismiss();

        if (profileData != null) {
            if (profileData.getName() != null) {
                mUserNameTxt.setText(profileData.getName());
            } else {
                mUserNameTxt.setText("");
            }

            if (profileData.getEmail() != null) {
                mUserEmailTxt.setText(profileData.getEmail());
            } else {
                mUserEmailTxt.setText("");
            }

            if (profileData.getAge().toString() != null){
                mUserAgeTxt.setText(profileData.getAge().toString());
            } else {
                mUserAgeTxt.setText("");
            }

            if (profileData.getMobile().toString() != null) {
                mUserPhoneTxt.setText(profileData.getMobile().toString());
            } else {
                mUserPhoneTxt.setText("");
            }

            if (profileData.getAddress() != null || profileData.getCity() != null || profileData.getState() != null) {
                mUserAddressTxt.setText(profileData.getAddress() + "\r\n" + profileData.getCity() + "\r\n" + profileData.getState());
            } else {
                mUserAddressTxt.setText("");
            }

            if (profileData.getEntryType() != null) {
                mUserTypeTxt.setText(profileData.getEntryType());
            } else {
                mUserTypeTxt.setText("");
            }
        }
    }

    @Override
    public void onResponseFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        Toast.makeText(mContext, "Something went wrong. Unable to fetch User Detail. Please try again.", Toast.LENGTH_SHORT).show();
    }
}
