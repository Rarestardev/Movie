package com.rarestardev.movie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rarestardev.movie.R;
import com.rarestardev.movie.databinding.ActivitySignInBinding;
import com.rarestardev.movie.utilities.Constants;
import com.rarestardev.movie.utilities.SecurePreferences;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient gApiClient;
    private ActivitySignInBinding binding;

    private static final int RC_SIGN_IN = 101;

    private SecurePreferences securePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);

        securePreferences = new SecurePreferences();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        binding.btnSignIn.setOnClickListener(view -> signIn());

    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN){
            assert data != null;
            GoogleSignInResult gsResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert gsResult != null;
            HandleSignInResult(gsResult);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void HandleSignInResult(GoogleSignInResult gsResult) {
        Log.d("SignIn","HandleSignInResult : " + gsResult.isSuccess());
        if (gsResult.isSuccess()){
            GoogleSignInAccount gAccount = gsResult.getSignInAccount();
            SetDataOnPref(gAccount);
        }else {
            SetDataOnPref(null);
        }
    }

    private void SetDataOnPref(GoogleSignInAccount inAccount){
        if (inAccount == null){
            // clear pref account
            securePreferences.clearAllSecurePreferences(SignInActivity.this, Constants.SHARED_PREF_NAME);
            return;
        }

        String username = inAccount.getDisplayName();
        String photoUrl = String.valueOf(inAccount.getPhotoUrl());

        Log.d("SignIn","PhotoUrl :" + photoUrl);

        securePreferences.saveSecureString(SignInActivity.this,Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_USERNAME,username);
        securePreferences.saveSecureString(SignInActivity.this,Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_PHOTO,photoUrl);

        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("SignIn","ConnectionResult : " + connectionResult);
    }
}