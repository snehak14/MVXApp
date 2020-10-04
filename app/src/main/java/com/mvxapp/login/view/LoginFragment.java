package com.mvxapp.login.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mvxapp.R;
import com.mvxapp.home.HomeActivity;
import com.mvxapp.login.listener.LoginContract;
import com.mvxapp.login.model.LoginResponseData;
import com.mvxapp.login.presenter.GetLoginInteractorImpl;
import com.mvxapp.login.presenter.GetRegisterInteractorImpl;
import com.mvxapp.login.presenter.LoginPresenterImpl;
import com.mvxapp.login.presenter.RegisterPresenterImpl;
import com.mvxapp.utils.MVXUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginFragment extends Fragment implements LoginContract.LoginView, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private Context mContext;

    @BindView(R.id.email_edtxt)
    EditText mEmailText;

    @BindView(R.id.pass_edtxt)
    EditText mPasswordText;

    private LoginButton loginButton;

    private ProgressDialog progressBar;

    private LoginContract.presenter mLoginPresenter;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private SignInButton signInButton;

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    private GoogleApiClient mGoogleApiClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("932583654213-lv9n48ms51q6uai426tutdtchet8n47b.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
//                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_layout, container, false);

        ButterKnife.bind(this, rootView);

        MVXUtils.hideStatusBar((Activity) mContext);

        signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();

                //starting the activity for result
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = view.findViewById(R.id.login_button);

       // loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @OnClick({R.id.facebooklogin_btn})
    public void callFacebookLogin(){
        loginButton.performClick();
    }

    @OnClick({R.id.googlelogin_btn})
    public void callGoogleLogin(){
        signInButton.performClick();
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
        mLoginPresenter = new LoginPresenterImpl(this, new GetLoginInteractorImpl(), mContext);
        mLoginPresenter.requestLogin(loginDetail);
    }

    @Override
    public void hideProgress() {
        progressBar.dismiss();
    }

    @Override
    public void setLogin(LoginResponseData loginData) {
        progressBar.dismiss();
        Toast.makeText(mContext, loginData.getMessage(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    @Override
    public void onResponseFailure(@Nullable Throwable throwable) {
        progressBar.dismiss();
        Toast.makeText(mContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            startActivity(new Intent(mContext, HomeActivity.class));
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

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
                firebaseAuthWithGoogle(account);
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
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        Toast.makeText(mContext, "User Signed In", Toast.LENGTH_SHORT).show();
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
      //  startActivity(new Intent(mContext, HomeActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
