package com.prm.carrental.ui.home.profile;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.network.model.UserResponse;
import com.prm.carrental.core.network.service.UserService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseFragment;
import com.prm.carrental.ui.auth.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Displays profile info and logout action.
 */
public class ProfileFragment extends BaseFragment {

    private static final String TAG = "ProfileFragment";
    private SessionManager sessionManager;
    private EditText emailField, fullNameField, driverLicenseField;
    private Button saveButton, logoutButton;

    @Override
    protected int layoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = ServiceLocator.sessionManager();

        emailField = view.findViewById(R.id.profileEmail);
        fullNameField = view.findViewById(R.id.profileFullName);
        driverLicenseField = view.findViewById(R.id.profileDriverLicense);
        saveButton = view.findViewById(R.id.buttonSaveProfile);
        logoutButton = view.findViewById(R.id.buttonLogout);

        // Hiển thị dữ liệu session hiện tại
        displaySessionData();

        // Nút Lưu
        saveButton.setOnClickListener(v -> saveProfile());

        // Nút Đăng xuất
        logoutButton.setOnClickListener(v -> {
            sessionManager.clear();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    /**
     * Hiển thị dữ liệu profile từ SessionManager lên UI
     */
    private void displaySessionData() {
        emailField.setText(sessionManager.getEmail() != null ? sessionManager.getEmail() : "");
        emailField.setEnabled(false);

        fullNameField.setText(sessionManager.getFullName() != null ? sessionManager.getFullName() : "");
        driverLicenseField.setText(sessionManager.getDriverLicenseNumber() != null ? sessionManager.getDriverLicenseNumber() : "");

        Log.d(TAG, "Session data: email=" + sessionManager.getEmail()
                + ", fullName=" + sessionManager.getFullName()
                + ", driverLicense=" + sessionManager.getDriverLicenseNumber());
    }

    /**
     * Gọi API cập nhật profile
     */
    private void saveProfile() {
        String newFullName = fullNameField.getText().toString().trim();
        String newLicense = driverLicenseField.getText().toString().trim();

        // Tạo request body
        UserService.UpdateUserRequest body = new UserService.UpdateUserRequest(newFullName, newLicense);

        ServiceLocator.apiClient()
                .createService(UserService.class)
                .updateUserProfile(sessionManager.getUserId(), body)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật session với dữ liệu vừa nhập
                            sessionManager.setFullName(newFullName);
                            sessionManager.setDriverLicenseNumber(newLicense);

                            // Cập nhật UI
                            displaySessionData();

                            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Update failed: " + response.code() + " - " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "API failure: ", t);
                    }
                });
    }


}
