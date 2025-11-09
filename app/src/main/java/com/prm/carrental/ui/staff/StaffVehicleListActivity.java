package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.VehicleDto;
import com.prm.carrental.core.network.service.StationsService;
import com.prm.carrental.core.network.service.VehicleService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.staff.adapters.StaffStationVehicleAdapter;
import com.prm.carrental.ui.staff.adapters.StaffVehicleAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffVehicleListActivity extends BaseActivity
        implements StaffVehicleAdapter.OnStaffVehicleClickListener{

    private RecyclerView rvVehiclesList;
    private StaffVehicleAdapter staffVehicleAdapter;
    private SessionManager sessionManager;
    private StationsService stationsService;
    private VehicleService vehicleService;
    private ApiClient apiClient;
    private Button btnPrev, btnNext;
    private TextView tvPage;
    private Toolbar toolbar;

    private int currentPage = 1;
    private int totalPages = 1;
    private final int PAGE_SIZE = 5;

    private String status;
    public static final String EXTRA_STATUS = "extra_status";

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_vehicle_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        status = getIntent().getStringExtra(EXTRA_STATUS);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách xe");
        }

        rvVehiclesList = findViewById(R.id.rvVehiclesList);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvPage = findViewById(R.id.tvPage);

        rvVehiclesList.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        vehicleService = apiClient.createService(VehicleService.class);

        loadVehicles(currentPage);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadVehicles(currentPage);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadVehicles(currentPage);
            }
        });
    }

    private void loadVehicles(int pageNumber) {

        vehicleService.getVehicles(pageNumber, PAGE_SIZE, status).enqueue(new Callback<ApiResponse<PagedResponse<VehicleDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                                   Response<ApiResponse<PagedResponse<VehicleDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PagedResponse<VehicleDto> vehicleListResponse = response.body().getValue();

                    if (vehicleListResponse != null && vehicleListResponse.getItems() != null) {
                        // Gán adapter
                        if (staffVehicleAdapter == null) {
                            // Lần đầu tiên: Tạo adapter mới
                            staffVehicleAdapter = new StaffVehicleAdapter(
                                    StaffVehicleListActivity.this,
                                    new ArrayList<>(vehicleListResponse.getItems()),
                                    StaffVehicleListActivity.this  // ← Truyền listener (this)
                            );
                            rvVehiclesList.setAdapter(staffVehicleAdapter);
                        } else {
                            // Các lần sau: Chỉ update data
                            staffVehicleAdapter.updateData(vehicleListResponse.getItems());
                        }

                        // Cập nhật thông tin phân trang
                        currentPage = vehicleListResponse.getPageNumber();
                        totalPages = vehicleListResponse.getTotalPages();

                        // Enable/Disable buttons
                        btnPrev.setEnabled(currentPage > 1);
                        btnNext.setEnabled(currentPage < totalPages);

                        tvPage.setText(currentPage + "/" + totalPages);
                    } else {
                        Toast.makeText(StaffVehicleListActivity.this, "Không có dữ liệu xe", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(StaffVehicleListActivity.this, "Không tải được danh sách xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                                  Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffVehicleListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

        @Override
    public void onStaffVehicleSelected(VehicleDto vehicle) {
        Intent intent = new Intent(this, StaffVehicleDetailActivity.class);
        intent.putExtra(StaffVehicleDetailActivity.EXTRA_VEHICLE_ID, vehicle.getId());
        intent.putExtra(StaffVehicleDetailActivity.EXTRA_STATION_ID, "");
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        onBackPressed(); // quay lại trang trước
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVehicles(currentPage);
    }
}