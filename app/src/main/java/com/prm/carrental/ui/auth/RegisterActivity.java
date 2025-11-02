package com.prm.carrental.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.RegisterRequest;
import com.prm.carrental.core.network.model.UserResponse;
import com.prm.carrental.core.network.service.AuthService;
import com.prm.carrental.core.ui.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Captures renter registration information.
 */
public class RegisterActivity extends BaseActivity {

    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputDriverLicense;
    private EditText inputIdCard;
    private Button buttonSubmit;
    private ProgressBar progressBar;

    private AuthService authService;
    private Call<ApiResponse<UserResponse>> currentCall;

    @Override
    protected int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = ServiceLocator.authService();
    }

    @Override
    protected void setupViews() {
        inputFullName = findViewById(R.id.inputFullName);
        inputEmail = findViewById(R.id.inputRegisterEmail);
        inputPassword = findViewById(R.id.inputRegisterPassword);
        inputDriverLicense = findViewById(R.id.inputDriverLicense);
        inputIdCard = findViewById(R.id.inputIdCard);
        buttonSubmit = findViewById(R.id.buttonSubmitRegister);
        progressBar = findViewById(R.id.progressRegister);

        buttonSubmit.setOnClickListener(v -> submitRegistration());
    }

    private void submitRegistration() {
        String fullName = inputFullName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString();
        String driverLicense = inputDriverLicense.getText().toString().trim();
        String idCard = inputIdCard.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            inputFullName.setError(getString(R.string.error_fullname_required));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_email_required));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_password_required));
            return;
        }

        RegisterRequest request = new RegisterRequest(
            fullName,
            email,
            password,
            TextUtils.isEmpty(driverLicense) ? null : driverLicense,
            TextUtils.isEmpty(idCard) ? null : idCard
        );

        setLoading(true);
        currentCall = authService.register(request);
        currentCall.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(
                Call<ApiResponse<UserResponse>> call,
                Response<ApiResponse<UserResponse>> response
            ) {
                setLoading(false);
                ApiResponse<UserResponse> body = response.body();
                if (body == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                if (!body.isSuccess()) {
                    showMessage(extractError(body));
                    return;
                }
                showMessage(getString(R.string.register_success));
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }
                setLoading(false);
                showMessage(getString(R.string.error_network, t.getLocalizedMessage()));
            }
        });
    }

    private String extractError(ApiResponse<?> response) {
        if (response.getErrors().isEmpty()) {
            return getString(R.string.error_generic);
        }
        return response.getErrors().get(0).getMessage();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        buttonSubmit.setEnabled(!loading);
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
