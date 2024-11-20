package com.rarestardev.movie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rarestardev.movie.R;
import com.rarestardev.movie.utilities.Constants;
import com.rarestardev.movie.utilities.SecurePreferences;

public class LogoActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 3000;
    private SecurePreferences securePreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        securePreferences = new SecurePreferences();


        String username = securePreferences.getSecureString(this, Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_USERNAME);


        new Handler().postDelayed(() -> {
            if (username.isEmpty()){

                securePreferences.clearAllSecurePreferences(LogoActivity.this,Constants.SHARED_PREF_NAME);
                intent = new Intent(LogoActivity.this,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            }else {

                intent = new Intent(LogoActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            startActivity(intent);
        },SPLASH_DURATION);
    }
}