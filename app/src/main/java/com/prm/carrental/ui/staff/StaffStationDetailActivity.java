package com.prm.carrental.ui.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.StationDto;
import com.prm.carrental.core.network.model.StationRequest;
import com.prm.carrental.core.network.service.StationsService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffStationDetailActivity extends BaseActivity {

    public static final String EXTRA_STATION_ID = "extra_station_id";
    public static final String EXTRA_FUNCTION = "func";
    private Toolbar toolbar;
    private EditText etLongitude, etLatitude, etStationAddress, etStationName;
    private Button btnAction, btnDeny;
    private SessionManager sessionManager;
    private ApiClient apiClient;
    private StationsService stationsService;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_station_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getIntent().getStringExtra(EXTRA_STATION_ID);
        String func = getIntent().getStringExtra(EXTRA_FUNCTION);

        toolbar = findViewById(R.id.toolbar);
        btnAction = findViewById(R.id.btnAction);
        btnDeny = findViewById(R.id.btnDeny);
        etStationName = findViewById(R.id.etStationName);
        etStationAddress = findViewById(R.id.etStationAddress);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);

        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if("Add".equalsIgnoreCase(func)){
                getSupportActionBar().setTitle("Thêm trạm mới");
            }else if("Edit".equalsIgnoreCase(func)){
                getSupportActionBar().setTitle("Cập nhật trạm");
            }
        }

        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        stationsService = apiClient.createService(StationsService.class);

        loadStation(id, func);

        btnAction.setOnClickListener(v -> {
            String stationName = etStationName.getText().toString();
            String stationAddress = etStationAddress.getText().toString();
            String latitudeText = etLatitude.getText().toString();
            String longitudeText = etLongitude.getText().toString();

            if(stationName.isEmpty() || stationAddress.isEmpty()
                    || latitudeText.isEmpty() || longitudeText.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitude, longitude;
            try{
                latitude = Double.parseDouble(latitudeText);
                longitude = Double.parseDouble(longitudeText);
                if("Add".equalsIgnoreCase(func)){
                    StationRequest request = new StationRequest(stationName, stationAddress, latitude, longitude);
                    stationsService.createStation(request).enqueue(new Callback<ApiResponse<StationDto>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<StationDto>> call, Response<ApiResponse<StationDto>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                StationDto result = response.body().getValue();
                                if (result != null) {
                                    Toast.makeText(StaffStationDetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(StaffStationDetailActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StaffStationDetailActivity.this, "Lỗi thêm trạm", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<StationDto>> call, Throwable t) {
                            Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                            Toast.makeText(StaffStationDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if("Edit".equalsIgnoreCase(func)){
                    StationRequest request = new StationRequest(stationName, stationAddress, latitude, longitude);
                    stationsService.updateStation(id, request).enqueue(new Callback<ApiResponse<Boolean>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Boolean result = response.body().getValue();
                                if (Boolean.TRUE.equals(result)) {
                                    Toast.makeText(StaffStationDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(StaffStationDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StaffStationDetailActivity.this, "Lỗi xóa người dùng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                            Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                            Toast.makeText(StaffStationDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Kinh độ & vĩ độ phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }

    private void loadStation(String stationId, String func){
        if ("Edit".equalsIgnoreCase(func)) {
            btnAction.setText("Cập nhật");
            stationsService.getStationById(stationId).enqueue(new Callback<ApiResponse<StationDto>>() {
                @Override
                public void onResponse(Call<ApiResponse<StationDto>> call,
                                       Response<ApiResponse<StationDto>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        StationDto stationResponse = response.body().getValue();

                        if (stationResponse != null) {
                            etStationName.setText(stationResponse.getName());
                            etStationAddress.setText(stationResponse.getAddress());
                            etLatitude.setText(String.valueOf(stationResponse.getLatitude()));
                            etLongitude.setText(String.valueOf(stationResponse.getLongitude()));
                        } else {
                            Toast.makeText(StaffStationDetailActivity.this, "Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(StaffStationDetailActivity.this, "Không tải được danh sách user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<StationDto>> call,
                                      Throwable t) {
                    Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                    Toast.makeText(StaffStationDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else if("Add".equalsIgnoreCase(func)){
            btnAction.setText("Thêm");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}