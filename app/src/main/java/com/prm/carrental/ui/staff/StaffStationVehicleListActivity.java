package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Lists vehicles for staff to manage.
 */
public class StaffStationVehicleListActivity extends BaseActivity
        implements StaffStationVehicleAdapter.OnStaffStationVehicleClickListener {

    private RecyclerView rvStationVehiclesList;
    private StaffStationVehicleAdapter staffStationVehicleAdapter;
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

    private String stationId;

    public static final String EXTRA_STATION_ID = "extra_station_id";

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_station_vehicle_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách xe");
        }
        stationId = getIntent().getStringExtra(EXTRA_STATION_ID);

        if (stationId == null) {
            Toast.makeText(this, "Không có Station ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        rvStationVehiclesList = findViewById(R.id.rvStationVehiclesList);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvPage = findViewById(R.id.tvPage);

        rvStationVehiclesList.setLayoutManager(new LinearLayoutManager(this));
        //rvStationVehiclesList.setAdapter(new StationVehiclesAdapter(this));

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        stationsService = apiClient.createService(StationsService.class);
        vehicleService = apiClient.createService(VehicleService.class);

        loadVehicles(currentPage, stationId);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadVehicles(currentPage, stationId);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadVehicles(currentPage, stationId);
            }
        });
    }

//    @Override
//    public void onStaffVehicleSelected(VehicleDto vehicle) {
//        Intent intent = new Intent(this, StaffVehicleDetailActivity.class);
//        intent.putExtra(StaffVehicleDetailActivity.EXTRA_VEHICLE_ID, vehicle.getId());
//        startActivity(intent);
//    }

    private void loadVehicles(int pageNumber, String stationId) {

        stationsService.getVehiclesInStation(stationId,pageNumber, PAGE_SIZE).enqueue(new Callback<ApiResponse<PagedResponse<VehicleDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                                   Response<ApiResponse<PagedResponse<VehicleDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PagedResponse<VehicleDto> vehicleListResponse = response.body().getValue();

                    if (vehicleListResponse != null && vehicleListResponse.getItems() != null) {
                        // Gán adapter
                        if (staffStationVehicleAdapter == null) {
                            // Lần đầu tiên: Tạo adapter mới
                            staffStationVehicleAdapter = new StaffStationVehicleAdapter(
                                    StaffStationVehicleListActivity.this,
                                    new ArrayList<>(vehicleListResponse.getItems()),
                                    StaffStationVehicleListActivity.this  // ← Truyền listener (this)
                            );
                            rvStationVehiclesList.setAdapter(staffStationVehicleAdapter);
                        } else {
                            // Các lần sau: Chỉ update data
                            staffStationVehicleAdapter.updateData(vehicleListResponse.getItems());
                        }

                        // Cập nhật thông tin phân trang
                        currentPage = vehicleListResponse.getPageNumber();
                        totalPages = vehicleListResponse.getTotalPages();

                        // Enable/Disable buttons
                        btnPrev.setEnabled(currentPage > 1);
                        btnNext.setEnabled(currentPage < totalPages);

                        tvPage.setText(currentPage + "/" + totalPages);
                    } else {
                        Toast.makeText(StaffStationVehicleListActivity.this, "Không có dữ liệu xe", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(StaffStationVehicleListActivity.this, "Không tải được danh sách xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                                  Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffStationVehicleListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed(); // quay lại trang trước
            return true;
        } else if (id == R.id.menu_add) {
            Intent intent = new Intent(this, StaffVehicleDetailActivity.class);
            //intent.putExtra(StaffVehicleDetailActivity.EXTRA_FUNCTION, "Add");
            intent.putExtra(StaffVehicleDetailActivity.EXTRA_STATION_ID, stationId);
            intent.putExtra(StaffVehicleDetailActivity.EXTRA_VEHICLE_ID, "");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onEditStation(VehicleDto vehicle) {
//        Intent intent = new Intent(this, StaffVehicleDetailActivity.class);
//        intent.putExtra(StaffVehicleDetailActivity.EXTRA_VEHICLE_ID, vehicle.getId());
//        intent.putExtra(StaffVehicleDetailActivity.EXTRA_FUNCTION, "Edit");
//        startActivity(intent);
//    }

    @Override
    public void onDeleteStation(VehicleDto vehicle) {
        vehicleService.deleteVehicleById(vehicle.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && Boolean.TRUE.equals(response.body().getValue())) {
                    Toast.makeText(StaffStationVehicleListActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadVehicles(currentPage, stationId); // reload danh sách
                } else {
                    Toast.makeText(StaffStationVehicleListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(StaffStationVehicleListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVehicles(currentPage, stationId);
    }
}
