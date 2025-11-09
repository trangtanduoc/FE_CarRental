package com.prm.carrental.ui.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.UserDto;
import com.prm.carrental.core.network.service.UserService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Allows staff to approve or reject renter verification.
 */
public class StaffUserDetailActivity extends BaseActivity {
    private TextView tvFullName,tvEmail,tvIdCardNumber,tvDriverLicenseNumber;

    private Toolbar toolbar;
    private Button btnDelete;
    public static final String EXTRA_USER_ID = "extra_user_id";
    private SessionManager sessionManager;
    private ApiClient apiClient;
    private UserService userService;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_user_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý người dùng");
        }
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvIdCardNumber = findViewById(R.id.tvIdCardNumber);
        tvDriverLicenseNumber = findViewById(R.id.tvDriverLicenseNumber);
        btnDelete = findViewById(R.id.btnDelete);

        String id = getIntent().getStringExtra(EXTRA_USER_ID);

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        userService = apiClient.createService(UserService.class);

        loadUser(id);

        btnDelete.setOnClickListener(v -> {
            userService.deleteUserById(id).enqueue(new Callback<ApiResponse<Boolean>>() {
                @Override
                public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Boolean result = response.body().getValue();
                        if (Boolean.TRUE.equals(result)) {
                            Toast.makeText(StaffUserDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(StaffUserDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StaffUserDetailActivity.this, "Lỗi xóa người dùng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                    Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                    Toast.makeText(StaffUserDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
            showMessage("Đã xóa");
            finish();
        });
    }

    private void loadUser(String userId) {
        userService.getUserById(userId).enqueue(new Callback<ApiResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserDto>> call,
                                   Response<ApiResponse<UserDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UserDto userResponse = response.body().getValue();

                    if (userResponse != null) {
                        tvFullName.setText(userResponse.getFullName());
                        tvEmail.setText(userResponse.getEmail());
                        tvDriverLicenseNumber.setText(userResponse.getDriverLicenseNumber());
                        tvIdCardNumber.setText(userResponse.getIdCardNumber());

                        // ⚙️ Kiểm tra điều kiện hiển thị nút
                        Toast.makeText(StaffUserDetailActivity.this, userResponse.getStatus(), Toast.LENGTH_LONG).show();
                        Toast.makeText(StaffUserDetailActivity.this, "Verified = " + userResponse.getIsVerified(), Toast.LENGTH_LONG).show();

                        if ("Active".equalsIgnoreCase(userResponse.getStatus())
                                && userResponse.getIsVerified()) {
                            btnDelete.setVisibility(Button.VISIBLE);
                        } else {
                            btnDelete.setVisibility(Button.GONE);
                        }
                    } else {
                        Toast.makeText(StaffUserDetailActivity.this, "Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(StaffUserDetailActivity.this, "Không tải được danh sách user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserDto>> call,
                                  Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffUserDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {  // ← nút back mặc định
            onBackPressed(); // Quay lại activity trước
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
