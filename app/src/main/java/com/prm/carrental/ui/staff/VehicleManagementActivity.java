package com.prm.carrental.ui.staff;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Allows staff to update vehicle status or battery.
 */
public class VehicleManagementActivity extends BaseActivity {

    static final String EXTRA_VEHICLE_NAME = "extra_vehicle_name";

    @Override
    protected int layoutId() {
        return R.layout.activity_vehicle_management;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button updateBattery = findViewById(R.id.buttonUpdateBattery);
        Button updateStatus = findViewById(R.id.buttonUpdateStatus);

        updateBattery.setOnClickListener(v -> showMessage("Cập nhật pin demo"));
        updateStatus.setOnClickListener(v -> showMessage("Đổi trạng thái demo"));
    }
}
