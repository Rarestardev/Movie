package com.rarestardev.movie.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePreferences {

    private SharedPreferences getEncryptedSharedPreferences(Context context, String prefsName) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            return EncryptedSharedPreferences.create(
                    prefsName,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void saveSecureString(Context context, String prefsName, String key, String value) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }


    public String getSecureString(Context context, String prefsName, String key) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        return encryptedSharedPreferences != null ? encryptedSharedPreferences.getString(key, "") : null;
    }


    public void saveSecureInt(Context context, String prefsName, String key, int value) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }


    public int getSecureInt(Context context, String prefsName, String key) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        return encryptedSharedPreferences != null ? encryptedSharedPreferences.getInt(key, 0) : -1;
    }


    public void saveSecureBoolean(Context context, String prefsName, String key, boolean value) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }


    public boolean getSecureBoolean(Context context, String prefsName, String key) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        return encryptedSharedPreferences != null && encryptedSharedPreferences.getBoolean(key, false);
    }


    public void removeSecureKey(Context context, String prefsName, String key) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }


    public void clearAllSecurePreferences(Context context, String prefsName) {
        SharedPreferences encryptedSharedPreferences = getEncryptedSharedPreferences(context, prefsName);
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
