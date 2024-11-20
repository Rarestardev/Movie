package com.rarestardev.movie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rarestardev.movie.R;
import com.rarestardev.movie.databinding.ActivityProfileBinding;
import com.rarestardev.movie.utilities.Constants;
import com.rarestardev.movie.utilities.SecurePreferences;
import com.squareup.picasso.Picasso;

/**
 * This class for show user information.
 * Setting app & sign out button.
 * with GoogleApiClient.
 *
 * @author Rarestardev
 */
public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private GoogleApiClient gApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Log.e("SignIn", "ApiClient" + connectionResult))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        doInitialization();
    }

    private void doInitialization() {
        binding.imageBack.setOnClickListener(view -> finish());

        String username = getIntent().getStringExtra("Username");
        String photo = getIntent().getStringExtra("Photo");

        if (username != null && photo != null){

            binding.setUsername(username);
            Picasso.get().load(photo).into(binding.profileImageView);

        }


        binding.btnSettings.setOnClickListener(view ->
                Toast.makeText(this, "This is demo mode", Toast.LENGTH_SHORT).show());

        binding.btnSignOut.setOnClickListener(view -> signOut());
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(gApiClient).setResultCallback(status -> {
            Log.d("ProfileActivity","Status : " + status.isSuccess());
            updateUI(status.isSuccess());
        });
    }

    private void updateUI(boolean status) {
        if (status){
            SecurePreferences securePreferences = new SecurePreferences();
            securePreferences.clearAllSecurePreferences(this, Constants.SHARED_PREF_NAME);

            Intent intent = new Intent(ProfileActivity.this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else {
            Toast.makeText(this, "Something wrong!", Toast.LENGTH_SHORT).show();
            Log.e("ProfileActivity","Update ui failed");
        }
    }
}