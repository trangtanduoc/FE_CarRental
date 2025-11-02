package com.prm.carrental.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.AuthResponse;
import com.prm.carrental.core.network.model.LoginRequest;
import com.prm.carrental.core.network.service.AuthService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.home.HomeActivity;
import com.prm.carrental.ui.staff.StaffHomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles login flow for both renters and staff based on API response.
 */
public class LoginActivity extends BaseActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button buttonLogin;
    private TextView navigateRegister;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private AuthService authService;
    private Call<ApiResponse<AuthResponse>> currentCall;

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = ServiceLocator.sessionManager();
        authService = ServiceLocator.authService();
        checkExistingSession();
    }

    @Override
    protected void setupViews() {
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        navigateRegister = findViewById(R.id.buttonNavigateRegister);
        progressBar = findViewById(R.id.progressLogin);

        buttonLogin.setOnClickListener(v -> performLogin());
        navigateRegister.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void checkExistingSession() {
        String token = sessionManager.getToken();
        if (!TextUtils.isEmpty(token)) {
            navigateAccordingToRole(sessionManager.getUserRole());
            finish();
        }
    }

    private void performLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_email_required));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_password_required));
            return;
        }

        setLoading(true);
        currentCall = authService.login(new LoginRequest(email, password));
        currentCall.enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(
                Call<ApiResponse<AuthResponse>> call,
                Response<ApiResponse<AuthResponse>> response
            ) {
                setLoading(false);
                ApiResponse<AuthResponse> body = response.body();
                if (body == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                if (!body.isSuccess()) {
                    showMessage(extractError(body));
                    return;
                }
                AuthResponse auth = body.getValue();
                if (auth == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                sessionManager.saveSession(auth);
                showMessage(getString(R.string.login_success));
                navigateAccordingToRole(auth.getRole());
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }
                setLoading(false);
                showMessage(getString(R.string.error_network, t.getLocalizedMessage()));
            }
        });
    }

    private void navigateAccordingToRole(@Nullable String role) {
        if ("Staff".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, StaffHomeActivity.class));
        } else {
            navigateToHome();
        }
    }

    private String extractError(ApiResponse<?> response) {
        if (response.getErrors().isEmpty()) {
            return getString(R.string.error_generic);
        }
        return response.getErrors().get(0).getMessage();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!loading);
        navigateRegister.setEnabled(!loading);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentCall != null) {
            currentCall.cancel();
            currentCall = null;
        }
    }
}
