package com.prm.carrental.ui.staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.prm.carrental.R;
import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.BatteryUpdateRequest;
import com.prm.carrental.core.network.model.VehicleDto;
import com.prm.carrental.core.network.model.VehicleRequest;
import com.prm.carrental.core.network.service.VehicleService;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffVehicleDetailActivity extends BaseActivity {

    public static final String EXTRA_VEHICLE_ID = "extra_vehicle_id";
    public static final String EXTRA_STATION_ID = "extra_station_id";
    //public static final String EXTRA_FUNCTION = "func";
    private Toolbar toolbar;
    private ImageView imgVehicle;
    private EditText etBattery, etPlateNumb;
    private Spinner spinnerType;
    private Button btnAction, btnDeny, btnSelectImage;

    private SessionManager sessionManager;
    private ApiClient apiClient;
    private VehicleService vehicleService;

    private Uri selectedImageUri = null;
    private String stationId, vehicleId;
    private VehicleDto currentVehicle;


    // Activity Result Launcher để chọn ảnh
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        Glide.with(this)
                                .load(selectedImageUri)
                                .placeholder(R.drawable.ic_vehicle)
                                .into(imgVehicle);
                        Toast.makeText(this, "Đã chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_vehicle_detail;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stationId = getIntent().getStringExtra(EXTRA_STATION_ID);
        vehicleId = getIntent().getStringExtra(EXTRA_VEHICLE_ID);

        toolbar = findViewById(R.id.toolbar);
        imgVehicle = findViewById(R.id.imgVehicle);
        btnAction = findViewById(R.id.btnAction);
        btnDeny = findViewById(R.id.btnDeny);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        etPlateNumb = findViewById(R.id.etPlateNumb);
        etBattery = findViewById(R.id.etBattery);
        spinnerType = findViewById(R.id.spinnerType);

        setupSpinners();

        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize API
        sessionManager = new SessionManager(this);
        apiClient = new ApiClient(sessionManager);
        vehicleService = apiClient.createService(VehicleService.class);

        if (vehicleId != null && !vehicleId.isEmpty()) {
            setupViewMode();
            loadVehicleDetails(vehicleId);
        } else if (stationId != null && !stationId.isEmpty()) {
            setupAddMode();
        } else {
            Toast.makeText(this, "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Button Deny
        btnDeny.setOnClickListener(v -> finish());
    }

    private void setupAddMode() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm xe mới");
        }

        btnAction.setText("Thêm xe");
        btnSelectImage.setVisibility(View.VISIBLE);

        // Enable edit fields
        etPlateNumb.setEnabled(true);
        etBattery.setEnabled(true);
        spinnerType.setEnabled(true);

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnAction.setOnClickListener(v -> addVehicle());
    }

    private void setupViewMode() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết xe");
        }

        btnAction.setText("Cập nhật Pin");
        btnSelectImage.setVisibility(View.GONE);

        // Disable edit fields (chỉ xem)
        etPlateNumb.setEnabled(false);
        spinnerType.setEnabled(false);
        etBattery.setEnabled(false);

        btnAction.setOnClickListener(v -> showUpdateBatteryDialog());
    }

    private void loadVehicleDetails(String vehicleId) {
        vehicleService.getVehicleById(vehicleId).enqueue(new Callback<ApiResponse<VehicleDto>>() {
            @Override
            public void onResponse(Call<ApiResponse<VehicleDto>> call,
                                   Response<ApiResponse<VehicleDto>> response) {

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("API_RESPONSE", "isSuccess: " + response.body().isSuccess());
                    currentVehicle = response.body().getValue();
                    Log.d("API_RESPONSE", "Vehicle data: " + (currentVehicle != null ? currentVehicle.getPlateNumber() : "null"));
                    if (currentVehicle != null) {
                        displayVehicleInfo(currentVehicle);
                    } else {
                        Toast.makeText(StaffVehicleDetailActivity.this,
                                "Không tìm thấy xe", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(StaffVehicleDetailActivity.this,
                            "Lỗi tải thông tin xe", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<VehicleDto>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(StaffVehicleDetailActivity.this,
                        "Lỗi mạng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayVehicleInfo(VehicleDto vehicle) {
        etPlateNumb.setText(vehicle.getPlateNumber());
        etBattery.setText(String.valueOf(vehicle.getBatteryLevel()));

        // Set spinner selection based on type
        int typePosition = getTypePosition(vehicle.getType());
        spinnerType.setSelection(typePosition);

        // Load image
        if (vehicle.getImageUrl() != null && !vehicle.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(vehicle.getImageUrl())
                    .placeholder(R.drawable.ic_vehicle)
                    .into(imgVehicle);
        }

        // ẨN/HIỆN NÚT ACTION
        String status = vehicle.getStatus();
        if ("InUse".equalsIgnoreCase(status) || "Maintenance".equalsIgnoreCase(status)) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setEnabled(true);
            btnDeny.setVisibility(View.VISIBLE);
            btnDeny.setEnabled(true);
        } else {
            btnAction.setVisibility(View.GONE);
            btnDeny.setVisibility(View.GONE);
        }
    }

    private int getTypePosition(String type) {
        switch (type) {
            case "Scooter":
            case "1":
                return 0;
            case "Car":
            case "2":
                return 1;
            case "Other":
            case "3":
                return 2;
            default:
                return 0;
        }
    }

    private void showUpdateBatteryDialog() {
        if (currentVehicle == null) {
            Toast.makeText(this, "Không có thông tin xe", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_update_battery, null);

        TextView tvVehiclePlate = dialogView.findViewById(R.id.tvVehiclePlate);
        TextView tvCurrentBattery = dialogView.findViewById(R.id.tvCurrentBattery);
        EditText etNewBattery = dialogView.findViewById(R.id.etNewBattery);

        tvVehiclePlate.setText("Biển số: " + currentVehicle.getPlateNumber());
        tvCurrentBattery.setText(currentVehicle.getBatteryLevel() + "%");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            String newBatteryText = etNewBattery.getText().toString().trim();

            if (newBatteryText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mức pin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int newBattery;
            try {
                newBattery = Integer.parseInt(newBatteryText);
                if (newBattery < 0 || newBattery > 100) {
                    Toast.makeText(this, "Pin phải từ 0-100%", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Pin phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            updateBatteryLevel(currentVehicle.getId(), newBattery);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateBatteryLevel(String vehicleId, int newBattery) {
        BatteryUpdateRequest request = new BatteryUpdateRequest(newBattery);

        vehicleService.updateBatteryLevel(vehicleId, request)
                .enqueue(new Callback<ApiResponse<VehicleDto>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<VehicleDto>> call,
                                           Response<ApiResponse<VehicleDto>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            currentVehicle = response.body().getValue();
                            if (currentVehicle != null) {
                                displayVehicleInfo(currentVehicle);
                                Toast.makeText(StaffVehicleDetailActivity.this,
                                        "Cập nhật pin thành công", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                            }
                        } else {
                            Toast.makeText(StaffVehicleDetailActivity.this,
                                    "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<VehicleDto>> call, Throwable t) {
                        Log.e("API_ERROR", "Lỗi API: " + t.getMessage(), t);
                        Toast.makeText(StaffVehicleDetailActivity.this,
                                "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSpinners() {
        // Spinner Type
        String[] types = {"Xe máy", "Ô tô", "Loại khác"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void addVehicle() {
        String plateNumber = etPlateNumb.getText().toString().trim();
        String batteryText = etBattery.getText().toString().trim();

        if (plateNumber.isEmpty() || batteryText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (stationId == null || stationId.isEmpty()) {
            Toast.makeText(this, "Thiếu thông tin trạm!", Toast.LENGTH_SHORT).show();
            return;
        }

        int batteryLevel;
        try {
            batteryLevel = Integer.parseInt(batteryText);
            if (batteryLevel < 0 || batteryLevel > 100) {
                Toast.makeText(this, "Pin phải từ 0-100%", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Pin phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        String typeValue = getTypeValue(spinnerType.getSelectedItemPosition());

        // Tạo RequestBody cho các field text
        RequestBody plateNumberBody = RequestBody.create(MediaType.parse("text/plain"), plateNumber);
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), typeValue);
        RequestBody batteryBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(batteryLevel));
        RequestBody stationIdBody = RequestBody.create(MediaType.parse("text/plain"), stationId);

        // Tạo MultipartBody.Part cho image
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            try {
                File imageFile = getFileFromUri(selectedImageUri);
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);

                Log.d("ADD_VEHICLE", "Image file: " + imageFile.getName() + ", size: " + imageFile.length());
            } catch (Exception e) {
                Log.e("ADD_VEHICLE", "Error creating image part: " + e.getMessage(), e);
                Toast.makeText(this, "Lỗi xử lý ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Log.d("ADD_VEHICLE", "PlateNumber: " + plateNumber);
        Log.d("ADD_VEHICLE", "Type: " + typeValue);
        Log.d("ADD_VEHICLE", "Battery: " + batteryLevel);
        Log.d("ADD_VEHICLE", "StationId: " + stationId);

        // Call API
        vehicleService.createVehicle(plateNumberBody, typeBody, batteryBody, stationIdBody, imagePart)
                .enqueue(new Callback<ApiResponse<VehicleDto>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<VehicleDto>> call,
                                           Response<ApiResponse<VehicleDto>> response) {
                        Log.d("API_RESPONSE", "Response code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("API_RESPONSE", "isSuccess: " + response.body().isSuccess());

                            VehicleDto result = response.body().getValue();
                            if (result != null) {
                                Toast.makeText(StaffVehicleDetailActivity.this,
                                        "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                if (response.body().getErrors() != null && !response.body().getErrors().isEmpty()) {
                                    Log.e("API_ERROR", "Errors: " + response.body().getErrors().toString());
                                }
                                Toast.makeText(StaffVehicleDetailActivity.this,
                                        "Thêm xe thất bại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                Log.e("API_ERROR", "Error response: " + errorBody);
                                Toast.makeText(StaffVehicleDetailActivity.this,
                                        "Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("API_ERROR", "Error reading error body: " + e.getMessage());
                                Toast.makeText(StaffVehicleDetailActivity.this,
                                        "Lỗi thêm xe", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<VehicleDto>> call, Throwable t) {
                        Log.e("API_ERROR", "Lỗi khi gọi API: " + t.getMessage(), t);
                        Toast.makeText(StaffVehicleDetailActivity.this,
                                "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private File getFileFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        return file;
    }

    private String getTypeValue(int position) {
        switch (position) {
            case 0: return "Scooter"; // Xe máy
            case 1: return "Car"; // Ô tô
            case 2: return "Other"; // Loại khác
            default: return "1";
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