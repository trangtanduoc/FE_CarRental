package com.prm.carrental.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.prm.carrental.core.network.model.AuthResponse;

/**
 * Stores JWT tokens using SharedPreferences as suggested in the guide.
 */
public class SessionManager {

    private static final String PREFS_NAME = "prm_session";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_FULL_NAME = "user_full_name";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(AuthResponse response) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, response.getAccessToken())
            .putString(KEY_ROLE, response.getRole())
            .putString(KEY_FULL_NAME, response.getFullName())
            .putString(KEY_USER_ID, response.getUserId())
            .apply();
    }

    @Nullable
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    @Nullable
    public String getUserRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    @Nullable
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, null);
    }

    @Nullable
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
