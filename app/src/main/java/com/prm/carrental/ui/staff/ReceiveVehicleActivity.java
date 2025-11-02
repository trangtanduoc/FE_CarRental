package com.prm.carrental.ui.staff;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Staff flow to receive returned vehicles.
 */
public class ReceiveVehicleActivity extends BaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_receive_vehicle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button confirmReceive = findViewById(R.id.buttonConfirmReceive);
        confirmReceive.setOnClickListener(v -> {
            showMessage("Đã nhận xe (demo)");
            finish();
        });
    }
}
