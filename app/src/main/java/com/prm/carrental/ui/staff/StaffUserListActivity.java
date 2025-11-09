package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.UserDto;
import com.prm.carrental.core.network.service.UserService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.staff.adapters.StaffUserAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Displays users waiting for verification.
 */
public class StaffUserListActivity extends BaseActivity implements StaffUserAdapter.OnUserClickListener {
    RecyclerView rvPendingUserList;
    private StaffUserAdapter staffUserAdapter;
    private SessionManager sessionManager;
    private UserService userService;
    private ApiClient apiClient;
    private Button btnPrev, btnNext;
    private TextView tvPage;
    private Toolbar toolbar;

    private int currentPage = 1;
    private int totalPages = 1;
    private final int PAGE_SIZE = 10;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_user_list;
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
        rvPendingUserList = findViewById(R.id.rvPendingUserList);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvPage = findViewById(R.id.tvPage);

        rvPendingUserList.setLayoutManager(new LinearLayoutManager(this));
        //rvPendingUserList.setAdapter(new PendingVerificationListAdapter(this));

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        userService = apiClient.createService(UserService.class);

        loadUsers(currentPage);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadUsers(currentPage);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadUsers(currentPage);
            }
        });
    }

    @Override
    public void onUserSelected(UserDto user) {
        Intent intent = new Intent(this, StaffUserDetailActivity.class);
        intent.putExtra(StaffUserDetailActivity.EXTRA_USER_ID, user.getId());
        startActivity(intent);
    }

    private void loadUsers(int pageNumber) {
        userService.getRenters(pageNumber, PAGE_SIZE, "Renter").enqueue(new Callback<ApiResponse<PagedResponse<UserDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResponse<UserDto>>> call,
                                   Response<ApiResponse<PagedResponse<UserDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PagedResponse<UserDto> userListResponse = response.body().getValue();

                    if (userListResponse != null && userListResponse.getItems() != null) {
                        // Gán adapter
                        if (staffUserAdapter == null) {
                            // Lần đầu tiên: Tạo adapter mới
                            staffUserAdapter = new StaffUserAdapter(
                                    StaffUserListActivity.this,
                                    new ArrayList<>(userListResponse.getItems()),
                                    StaffUserListActivity.this  // ← Truyền listener (this)
                            );
                        rvPendingUserList.setAdapter(staffUserAdapter);
                        } else {
                            // Các lần sau: Chỉ update data
                            staffUserAdapter.updateData(userListResponse.getItems());
                        }

                        // Cập nhật thông tin phân trang
                        currentPage = userListResponse.getPageNumber();
                        totalPages = userListResponse.getTotalPages();

                        // Enable/Disable buttons
                        btnPrev.setEnabled(currentPage > 1);
                        btnNext.setEnabled(currentPage < totalPages);

                        tvPage.setText(currentPage + "/" + totalPages);
                    } else {
                        Toast.makeText(StaffUserListActivity.this, "Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(StaffUserListActivity.this, "Không tải được danh sách user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PagedResponse<UserDto>>> call,
                                  Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffUserListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
