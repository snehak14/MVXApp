package com.mvxapp.login.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mvxapp.R;
import com.mvxapp.home.HomeActivity;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;
import com.mvxapp.login.presenter.GetRegisterInteractorImpl;
import com.mvxapp.login.presenter.RegisterPresenterImpl;
import com.mvxapp.utils.MVXUtils;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.mvxapp.R.id.signup_btn;

public class SignupFragment extends Fragment implements RegisterContract.RegisterView{
    private static final String LOG_TAG = SignupFragment.class.getSimpleName();
    private Context mContext;

    @BindView(R.id.name_edtxt)
    EditText mNameText;

    @BindView(R.id.emailid_edtxt)
    EditText mEmailText;

    @BindView(R.id.username_edtxt)
    EditText mUsernameText;

    @BindView(R.id.password_edtxt)
    EditText mPasswordText;

    @BindView(R.id.address_edtxt)
    EditText mAddressText;

    @BindView(R.id.age_edtxt)
    EditText mAgeText;

    @BindView(R.id.mobile_edtxt)
    EditText mMobileText;

    @BindView(R.id.select_entrytype)
    Spinner mEntryTypeSelect;

    private ProgressDialog progressBar;

    private RegisterContract.presenter mRegisterPresenter;

    private RegisterResponseData mRegisterResponseData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mContext = context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup_layout, container, false);

        ButterKnife.bind(this, rootView);

        MVXUtils.hideStatusBar((Activity) mContext);

        return rootView;
    }

    @OnClick({signup_btn})
    public void validateData(){
        if (mNameText.getText().toString().trim() == null || mNameText.getText().toString().length() == 0){
            mNameText.setError("Please enter Name");
        } else if (mEmailText.getText().toString() == null || mEmailText.getText().toString().equalsIgnoreCase("")){
            mEmailText.setError("Please enter Email Id");
        } else if (mUsernameText.getText().toString() == null || mUsernameText.getText().toString().equalsIgnoreCase("")){
            mUsernameText.setError("Please enter Username");
        } else if (mPasswordText.getText().toString() == null || mPasswordText.getText().toString().equalsIgnoreCase("")){
            mPasswordText.setError("Please enter Password");
        } else if (mAddressText.getText().toString() == null || mAddressText.getText().toString().equalsIgnoreCase("")){
            mAddressText.setError("Please enter Address");
        } else if (mAgeText.getText().toString() == null || mAgeText.getText().toString().equalsIgnoreCase("")){
            mAgeText.setError("Please enter Age");
        } else if (mMobileText.getText().toString() == null || mMobileText.getText().toString().equalsIgnoreCase("")){
            mMobileText.setError("Please enter Mobile Number");
        } else if (mEntryTypeSelect.getSelectedItem().toString() == null || mEntryTypeSelect.getSelectedItem().toString().equalsIgnoreCase(mContext.getString(R.string.prompt_entry))){
            Toast.makeText(mContext, "Please select Entry Type", Toast.LENGTH_SHORT).show();
        } else {
            callRegisterAPI();
        }
    }
        public void callRegisterAPI(){
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("email", mEmailText.getText().toString());
            paramObject.put("name", mNameText.getText().toString());
            paramObject.put("password", mPasswordText.getText().toString());
            paramObject.put("mobile", Long.parseLong(mMobileText.getText().toString()));
            paramObject.put("age", Integer.parseInt(mAgeText.getText().toString()));
            paramObject.put("city", "");
            paramObject.put("state", "");
            paramObject.put("address", mAddressText.getText().toString());
            paramObject.put("entryType", mEntryTypeSelect.getSelectedItem());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody userDetail = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
        mRegisterPresenter = new RegisterPresenterImpl(this, new GetRegisterInteractorImpl(), mContext);
        mRegisterPresenter.requestRegister(userDetail);
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Set views to null:
    }

    @Override
    public void hideProgress() {
        progressBar.dismiss();
    }

    @Override
    public void setRegister(RegisterResponseData registerData) {
        progressBar.dismiss();
        Toast.makeText(mContext, registerData.getMessage(), Toast.LENGTH_SHORT).show();
        if (registerData != null) {
            mRegisterResponseData = registerData;
            showDialog(mContext, "", mRegisterResponseData);
        }
    }

    @Override
    public void otpVerify(LoginResponseData loginData) {
        progressBar.dismiss();
        Toast.makeText(mContext, loginData.getMessage(), Toast.LENGTH_SHORT).show();
        if (mRegisterResponseData.getData().getIsEmailVerified()) {
            Intent myIntent = new Intent(mContext, UnityPlayerActivity.class);
            // Needed to set component to remove explicit activity entry in application's manifest
            final ComponentName component = new ComponentName(mContext, UnityPlayerActivity.class);
            myIntent.setComponent(component);
            mContext.startActivity(myIntent);
        } else {
            Toast.makeText(mContext, mRegisterResponseData.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        assert throwable != null;
        Toast.makeText(mContext, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtpFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        assert throwable != null;
        Toast.makeText(mContext, "Otp Verification Failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resendOtp(ResendOtpResponseData resendOtpResponseData) {
        progressBar.dismiss();
        Toast.makeText(mContext, resendOtpResponseData.getMessage(), Toast.LENGTH_SHORT).show();
        showDialog(mContext, "", mRegisterResponseData);
    }

    @Override
    public void onResendFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        assert throwable != null;
        Toast.makeText(mContext, "Otp Resend Failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    public void showDialog(Context activity, String msg, RegisterResponseData registerData){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialogbox_otp);
        //Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText otpEdit = dialog.findViewById(R.id.otp_editTxt);
        otpEdit.setText(msg);

        Button dialogBtn_cancel = dialog.findViewById(R.id.btn_resend);
        dialogBtn_cancel.setOnClickListener(v -> {
            callResendOTPAPI(registerData);
            dialog.dismiss();
        });

        Button dialogBtn_okay = dialog.findViewById(R.id.btn_submit);
        dialogBtn_okay.setOnClickListener(v -> {
            callVerifyOTP(otpEdit.getText().toString(), registerData);
            dialog.cancel();
        });

        dialog.show();
    }

    private void callResendOTPAPI(RegisterResponseData registerData) {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        String countryCode = GetCountryZipCode();
        String contactNum = registerData.getData().getMobile().toString();
        Long mobileNumber = Long.parseLong(countryCode+contactNum);

        mRegisterPresenter = new RegisterPresenterImpl(this, new GetRegisterInteractorImpl(), mContext);
        mRegisterPresenter.resendOtp(mobileNumber);
    }

    private void callVerifyOTP(String otp, RegisterResponseData registerData) {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        String countryCode = GetCountryZipCode();

        JSONObject paramObject = new JSONObject();

        JSONObject userData = new JSONObject();
        try {
            userData.put("id", registerData.getData().getId());
            userData.put("name", registerData.getData().getName());
            userData.put("email", registerData.getData().getEmail());
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            paramObject.put("mobile", countryCode + registerData.getData().getMobile() + "");
            paramObject.put("otp", otp);
            paramObject.put("user", userData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody userOtp = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
        mRegisterPresenter = new RegisterPresenterImpl(this, new GetRegisterInteractorImpl(), mContext);
        mRegisterPresenter.requestOtp(userOtp);
    }

    private String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}
