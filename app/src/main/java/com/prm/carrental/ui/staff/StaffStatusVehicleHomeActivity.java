package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

public class StaffStatusVehicleHomeActivity extends BaseActivity{

    private Button btnMaintenance, btnAvailable, btnInUse, btnBooked;
    private Toolbar toolbar;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_status_vehicle_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hiển thị nút back mặc định trên Toolbar
        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnMaintenance = findViewById(R.id.btnMaintenance);
        btnAvailable = findViewById(R.id.btnAvailable);
        btnInUse = findViewById(R.id.btnInUse);
        btnBooked = findViewById(R.id.btnBooked);

        btnMaintenance.setOnClickListener(v -> {
            Intent intent = new Intent(StaffStatusVehicleHomeActivity.this, StaffVehicleListActivity.class);
            intent.putExtra(StaffVehicleListActivity.EXTRA_STATUS, "Maintenance");
            startActivity(intent);
        });

        btnAvailable.setOnClickListener(v -> {
            Intent intent = new Intent(StaffStatusVehicleHomeActivity.this, StaffVehicleListActivity.class);
            intent.putExtra(StaffVehicleListActivity.EXTRA_STATUS, "Available");
            startActivity(intent);
        });

        btnInUse.setOnClickListener(v -> {
            Intent intent = new Intent(StaffStatusVehicleHomeActivity.this, StaffVehicleListActivity.class);
            intent.putExtra(StaffVehicleListActivity.EXTRA_STATUS, "InUse");
            startActivity(intent);
        });

        btnBooked.setOnClickListener(v -> {
            Intent intent = new Intent(StaffStatusVehicleHomeActivity.this, StaffVehicleListActivity.class);
            intent.putExtra(StaffVehicleListActivity.EXTRA_STATUS, "Booked");
            startActivity(intent);
        });
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