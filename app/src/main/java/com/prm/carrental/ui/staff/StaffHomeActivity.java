package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Staff landing screen with shortcuts to verification and vehicle management.
 */
public class StaffHomeActivity extends BaseActivity {

    private Button btnManageUsers, btnManageStationAndVehicle, btnManageStatusVehicle;

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageStationAndVehicle = findViewById(R.id.btnManageStationAndVehicle);
        btnManageStatusVehicle = findViewById(R.id.btnManageStatusVehicle);

        btnManageUsers.setOnClickListener(v ->
            startActivity(new Intent(this, StaffUserListActivity.class))
        );

        btnManageStationAndVehicle.setOnClickListener(v ->
            startActivity(new Intent(this, StaffStationListActivity.class))
        );

        btnManageStatusVehicle.setOnClickListener(v ->
            startActivity(new Intent(this, StaffStatusVehicleHomeActivity.class))
        );
    }
}
