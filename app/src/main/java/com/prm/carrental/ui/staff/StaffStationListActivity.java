package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.StationDto;
import com.prm.carrental.core.network.service.StationsService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.staff.adapters.StaffStationAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffStationListActivity extends BaseActivity
        implements StaffStationAdapter.OnStaffStationClickListener {
    private RecyclerView rvStationList;
    private StaffStationAdapter staffStationAdapter;
    private SessionManager sessionManager;
    private StationsService stationsService;
    private ApiClient apiClient;
    private Button btnPrev, btnNext;
    private TextView tvPage;
    private Toolbar toolbar;
    private int currentPage = 1;
    private int totalPages = 1;
    private final int PAGE_SIZE = 5;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_station_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách trạm");
        }

        rvStationList = findViewById(R.id.rvStationList);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvPage = findViewById(R.id.tvPage);

        rvStationList.setLayoutManager(new LinearLayoutManager(this));
        //rvPendingUserList.setAdapter(new PendingVerificationListAdapter(this));

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        stationsService = apiClient.createService(StationsService.class);

        loadStations(currentPage);

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadStations(currentPage);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadStations(currentPage);
            }
        });
    }

    @Override
    public void onStaffStationSelected(StationDto station) {
        Intent intent = new Intent(StaffStationListActivity.this, StaffStationVehicleListActivity.class);
        intent.putExtra(StaffStationVehicleListActivity.EXTRA_STATION_ID, station.getId());
        startActivity(intent);
    }

    private void loadStations(int pageNumber) {
        stationsService.getStations(pageNumber, PAGE_SIZE).enqueue(new Callback<ApiResponse<PagedResponse<StationDto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PagedResponse<StationDto>>> call,
                                   Response<ApiResponse<PagedResponse<StationDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PagedResponse<StationDto> stationListResponse = response.body().getValue();

                    if (stationListResponse != null && stationListResponse.getItems() != null) {
                        // Gán adapter
                        if (staffStationAdapter == null) {
                            // Lần đầu tiên: Tạo adapter mới
                            staffStationAdapter = new StaffStationAdapter(
                                    StaffStationListActivity.this,
                                    new ArrayList<>(stationListResponse.getItems()),
                                    StaffStationListActivity.this  // ← Truyền listener (this)
                            );
                            rvStationList.setAdapter(staffStationAdapter);
                        } else {
                            // Các lần sau: Chỉ update data
                            staffStationAdapter.updateData(stationListResponse.getItems());
                        }

                        // Cập nhật thông tin phân trang
                        currentPage = stationListResponse.getPageNumber();
                        totalPages = stationListResponse.getTotalPages();

                        // Enable/Disable buttons
                        btnPrev.setEnabled(currentPage > 1);
                        btnNext.setEnabled(currentPage < totalPages);

                        tvPage.setText(currentPage + "/" + totalPages);
                    } else {
                        Toast.makeText(StaffStationListActivity.this, "Không có dữ liệu trạm", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(StaffStationListActivity.this, "Không tải được danh sách trạm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PagedResponse<StationDto>>> call,
                                  Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffStationListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(this, StaffStationDetailActivity.class);
            intent.putExtra(StaffStationDetailActivity.EXTRA_FUNCTION, "Add");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditStation(StationDto station) {
        Intent intent = new Intent(this, StaffStationDetailActivity.class);
        intent.putExtra(StaffStationDetailActivity.EXTRA_STATION_ID, station.getId());
        intent.putExtra(StaffStationDetailActivity.EXTRA_FUNCTION, "Edit");
        startActivity(intent);
    }

    @Override
    public void onDeleteStation(StationDto station) {
        stationsService.deleteStationById(station.getId()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && Boolean.TRUE.equals(response.body().getValue())) {
                    Toast.makeText(StaffStationListActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadStations(currentPage); // reload danh sách
                } else {
                    Toast.makeText(StaffStationListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                Toast.makeText(StaffStationListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload lại danh sách mỗi khi quay về màn hình này
        loadStations(currentPage);
    }
}