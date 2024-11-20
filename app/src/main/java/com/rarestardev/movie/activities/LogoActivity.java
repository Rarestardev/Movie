package com.rarestardev.movie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rarestardev.movie.R;
import com.rarestardev.movie.utilities.Constants;
import com.rarestardev.movie.utilities.SecurePreferences;

/**
 * this class for show app icon or logo app.
 * Check the user sign in or not.
 * User sign in == true (StartMainActivity).
 * User sign in == false (StartSignInActivity) for register User
 * with google api signIn Options.
 *
 * @author Rarestardev
 */

public class LogoActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 3000;
    private SecurePreferences securePreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        securePreferences = new SecurePreferences();

        // check securePreferences for register account or not
        String username = securePreferences.getSecureString(this, Constants.SHARED_PREF_NAME,Constants.SHARED_PREF_KEY_USERNAME);


        // logo activity handler with delayed 3000
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