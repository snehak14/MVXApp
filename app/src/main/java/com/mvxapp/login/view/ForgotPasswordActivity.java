package com.mvxapp.login.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mvxapp.R;
import com.mvxapp.common.BaseActivity;
import com.mvxapp.login.listener.ForgotPasswordContract;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.ForgotPasswordResponseData;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.presenter.ForgotPasswordPresenterImpl;
import com.mvxapp.login.presenter.GetForgotPasswordInteractorImpl;
import com.mvxapp.login.presenter.GetRegisterInteractorImpl;
import com.mvxapp.login.presenter.RegisterPresenterImpl;
import com.mvxapp.utils.MVXUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordContract.ResetPasswordView {

    @BindView(R.id.email_edtxt)
    EditText mEmailText;

    private ProgressDialog progressBar;

    private Context mContext;

    private ForgotPasswordContract.presenter mForgotPasswordPresenter;

    EditText emailEdit, otpEdit, passwordEdit, confirmpassEdit;

    @Override
    protected void networkState(boolean netAvailable) {
        if (!netAvailable){
            Toast.makeText(mContext, "Internet not available. Please check and re-try", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);

        MVXUtils.hideStatusBar(this);

        mContext = ForgotPasswordActivity.this;
    }

    @OnClick({R.id.submit_btn})
    public void getOTP(){
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        mForgotPasswordPresenter = new ForgotPasswordPresenterImpl(this, new GetForgotPasswordInteractorImpl(), mContext);
        if (mEmailText.getText().toString() == null || mEmailText.getText().toString().trim().length() < 1){
            mEmailText.setError("Please enter registered Email Id");
        } else {
            mForgotPasswordPresenter.requestPasswordOTP(mEmailText.getText().toString());
        }
    }

    @Override
    public void hideProgress() {
        progressBar.dismiss();
    }

    @Override
    public void setPassword(ForgotPasswordResponseData forgotPasswordResponseData) {
        progressBar.dismiss();
        showDialog(mContext, "", forgotPasswordResponseData);
    }

    private void showDialog(Context activity, String msg, ForgotPasswordResponseData forgotPasswordResponseData) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_reset_password);

        emailEdit = dialog.findViewById(R.id.email_edtxt);
        otpEdit = dialog.findViewById(R.id.otp_edtxt);
        passwordEdit = dialog.findViewById(R.id.password_edtxt);
        confirmpassEdit = dialog.findViewById(R.id.confirmpass_edtxt);

        Button dialogBtn_reset = dialog.findViewById(R.id.reset_btn);
        dialogBtn_reset.setOnClickListener(v -> {
            validateResetPassword(emailEdit.getText().toString(), otpEdit.getText().toString(), passwordEdit.getText().toString(),
                    confirmpassEdit.getText().toString(), forgotPasswordResponseData);
            dialog.cancel();
        });

        dialog.show();
    }

    private void validateResetPassword(String email, String otp, String password, String confirmpassword, ForgotPasswordResponseData forgotPasswordResponseData) {
        if (email == null || email.trim().length() < 1){
            emailEdit.setError("Please enter registered Email Id");
        } else if (otp == null || otp.trim().length() < 1){
            otpEdit.setError("Please enter OTP");
        } else if (password == null || password.trim().length() < 1){
            passwordEdit.setError("Please enter New Password");
        } else if (confirmpassword == null || confirmpassword.trim().length() < 1){
            confirmpassEdit.setError("Please enter confirm password");
        } else {
            callResetPassword(email, otp, password, confirmpassword, forgotPasswordResponseData);
        }
    }

    private void callResetPassword(String email, String otp, String password, String confirmpass, ForgotPasswordResponseData forgotPasswordResponseData) {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        String countryCode = MVXUtils.GetCountryZipCode(mContext);
        Long mobileNumber = Long.parseLong(91+""+forgotPasswordResponseData.getUser().getMobile());

        JSONObject userData = new JSONObject();
        try {
            userData.put("password", password);
            userData.put("confirmPassword", confirmpass);
            userData.put("email", email);
            userData.put("id", forgotPasswordResponseData.getUser().getId());
            userData.put("mobile", forgotPasswordResponseData.getUser().getMobile());
            userData.put("otp", otp);
        } catch (Exception e){
            e.printStackTrace();
        }
        RequestBody resetPassword = RequestBody.create(MediaType.parse("application/json"), userData.toString());
        mForgotPasswordPresenter = new ForgotPasswordPresenterImpl(this, new GetForgotPasswordInteractorImpl(), mContext);
        mForgotPasswordPresenter.requestResetPassword(resetPassword);
    }

    @Override
    public void setResetPassword(LoginResponseData resetPasswordResponseData) {
        progressBar.dismiss();
        Toast.makeText(mContext, resetPasswordResponseData.getMessage(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onResponsePasswordFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        Toast.makeText(mContext, "Unable to send OTP to registered mobile number. Try again later.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseResetPasswordFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        Toast.makeText(mContext, "Unable to reset password. Try again later.", Toast.LENGTH_SHORT).show();
    }
}
