package com.mvxapp.login.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mvxapp.R;
import com.mvxapp.home.HomeActivity;
import com.mvxapp.login.listener.LoginContract;
import com.mvxapp.login.listener.RegisterContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.model.RegisterResponseData;
import com.mvxapp.login.model.ResendOtpResponseData;
import com.mvxapp.login.presenter.GetLoginInteractorImpl;
import com.mvxapp.login.presenter.GetRegisterInteractorImpl;
import com.mvxapp.login.presenter.LoginPresenterImpl;
import com.mvxapp.login.presenter.RegisterPresenterImpl;
import com.mvxapp.utils.MVXUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginFragment extends Fragment implements LoginContract.LoginView, GoogleApiClient.OnConnectionFailedListener, RegisterContract.RegisterView {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private Context mContext;

    @BindView(R.id.email_edtxt)
    EditText mEmailText;

    @BindView(R.id.pass_edtxt)
    EditText mPasswordText;

    @BindView(R.id.signupquery_txt)
    TextView mSignUpText;

    private LoginButton loginButton;

    private ProgressDialog progressBar;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    private GoogleApiClient mGoogleApiClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    private LoginResponseData mLoginResponseData;

    private RegisterContract.presenter mRegisterPresenter;
    private RegisterResponseData mRegisterResponseData;
    String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mContext = context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(Objects.requireNonNull(getActivity()).getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                startActivity(new Intent(mContext, HomeActivity.class));
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("932583654213-edjr6hni7qr4tbt4p8dcglercm7va4b4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_layout, container, false);

        ButterKnife.bind(this, rootView);

        MVXUtils.hideStatusBar((Activity) mContext);

        SignInButton signInButton = rootView.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> {
            signIn();
        });

        initView(rootView);

        sharedPreferences = mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(View rootView) {
        SpannableString ss = new SpannableString(mContext.getString(R.string.signup_questxt));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {
                ((LoginActivity) Objects.requireNonNull(getActivity())).mViewPager.setCurrentItem(1);
            }
            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
               // ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 22, 30, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        mSignUpText.setText(ss);
        mSignUpText.setMovementMethod(LinkMovementMethod.getInstance());
        mSignUpText.setHighlightColor(mContext.getColor(R.color.tab_indicator));
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

    }

    @OnClick({R.id.facebooklogin_btn})
    public void callFacebookLogin(){
        loginButton.performClick();
    }

    @OnClick({R.id.googlelogin_btn})
    public void callGoogleLogin(){
        signIn();
    }

    @OnClick({R.id.forgotpass_txt})
    public void callForgotPassword(){
        Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @OnClick({R.id.login_btn})
    public void callLoginAPI(){
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
            paramObject.put("password", mPasswordText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody loginDetail = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
        LoginContract.presenter mLoginPresenter = new LoginPresenterImpl(this, new GetLoginInteractorImpl(), mContext);
        mLoginPresenter.requestLogin(loginDetail);
    }

    @Override
    public void hideProgress() {
        progressBar.dismiss();
    }

    @Override
    public void setRegister(RegisterResponseData registerData) {
        Toast.makeText(mContext, registerData.getMessage(), Toast.LENGTH_SHORT).show();
        if (registerData != null){
            mRegisterResponseData = registerData;
            if (!registerData.getData().getIsEmailVerified()){
                showDialog(mContext, "", registerData);
            }
        }
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

    @Override
    public void otpVerify(LoginResponseData loginData) {
        progressBar.dismiss();
        Toast.makeText(mContext, loginData.getMessage(), Toast.LENGTH_SHORT).show();
        if (mRegisterResponseData.getData().getIsEmailVerified()) {
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
//            Intent myIntent = new Intent(mContext, UnityPlayerActivity.class);
//            // Needed to set component to remove explicit activity entry in application's manifest
//            final ComponentName component = new ComponentName(mContext, UnityPlayerActivity.class);
//            myIntent.setComponent(component);
//            mContext.startActivity(myIntent);
        } else {
            Toast.makeText(mContext, mRegisterResponseData.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setLogin(LoginResponseData loginData) {
        progressBar.dismiss();
        Toast.makeText(mContext, loginData.getMessage(), Toast.LENGTH_SHORT).show();
        if (loginData != null) {
            mLoginResponseData = loginData;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loginToken", mLoginResponseData.getToken());
            editor.putString("userID", mLoginResponseData.getData().getId());
            editor.apply();
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
        }
    }

    @Override
    public void onResponseFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        Toast.makeText(mContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void OnSocialFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        assert throwable != null;
        Toast.makeText(mContext, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSocialResponseFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        assert throwable != null;
        Toast.makeText(mContext, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile user = Profile.getCurrentProfile();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            callRegisterAPI(response);
                            // Application code
//                            try {
//                                String email = object.getString("email");
//                                String birthday = object.getString("birthday");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();

                            //mContext.startActivity(new Intent(mContext, HomeActivity.class))
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(mContext, "Unable to login with Facebook. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    };

    public void callRegisterAPI(GraphResponse user){
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("email", user.getJSONObject().getString("email"));
            paramObject.put("name", user.getJSONObject().getString("first_name"));
            paramObject.put("password", "");
            paramObject.put("mobile", 0);
            paramObject.put("age", 0);
            paramObject.put("city", "");
            paramObject.put("state", "");
            paramObject.put("address", "");
            paramObject.put("entryType", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody userDetail = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
        mRegisterPresenter = new RegisterPresenterImpl(this, new GetRegisterInteractorImpl(), mContext);
        mRegisterPresenter.socialLogin(userDetail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (ApiException e) {
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mContext, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        progressBar = new ProgressDialog(mContext);
                        progressBar.setCancelable(false);//you can cancel it by pressing back button
                        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
                        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressBar.setProgress(0);//initially progress is 0
                        progressBar.setMax(100);//sets the maximum value 100
                        progressBar.show();//displays the progress bar
                        JSONObject paramObject = new JSONObject();
                        try {
                            paramObject.put("email", user.getEmail());
                            paramObject.put("name", user.getDisplayName());
                            paramObject.put("password", "");
                            if (user.getPhoneNumber() != null && !user.getPhoneNumber().equalsIgnoreCase("")) {
                                paramObject.put("mobile", Long.parseLong(user.getPhoneNumber()));
                            }
                            else {
                                paramObject.put("mobile", 0);
                            }
                            paramObject.put("age", 25);
                            paramObject.put("city", "");
                            paramObject.put("state", "");
                            paramObject.put("address", "");
                            paramObject.put("entryType", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody userDetail = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());
                        mRegisterPresenter = new RegisterPresenterImpl(this, new GetRegisterInteractorImpl(), mContext);
                        mRegisterPresenter.socialLogin(userDetail);

//                        Toast.makeText(mContext, "User Signed In", Toast.LENGTH_SHORT).show();
//                        mContext.startActivity(new Intent(mContext, HomeActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(mContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (mLoginResponseData != null && mLoginResponseData.getToken() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("loginToken", mLoginResponseData.getToken());
            editor.putString("userID", mLoginResponseData.getData().getId());
            editor.apply();
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void callResendOTPAPI(RegisterResponseData registerData) {
        progressBar = new ProgressDialog(mContext);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage(mContext.getResources().getString(R.string.process_txt));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();//displays the progress bar

        String countryCode = MVXUtils.GetCountryZipCode(mContext);
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

        String countryCode = MVXUtils.GetCountryZipCode(mContext);

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
}
