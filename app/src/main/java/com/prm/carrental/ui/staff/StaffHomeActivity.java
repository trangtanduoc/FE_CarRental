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

    @Override
    protected int layoutId() {
        return R.layout.activity_staff_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button pendingUsers = findViewById(R.id.buttonPendingUsers);
        Button manageVehicles = findViewById(R.id.buttonManageVehicles);

        pendingUsers.setOnClickListener(v ->
            startActivity(new Intent(this, PendingVerificationListActivity.class))
        );
        manageVehicles.setOnClickListener(v ->
            startActivity(new Intent(this, StationVehiclesActivity.class))
        );
    }
}
