package com.prm.carrental.ui.home.station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.payment.PaymentActivity;

/**
 * Shows vehicle detail info and allows renter to start a rental.
 */
public class VehicleDetailActivity extends BaseActivity {

    public static final String EXTRA_VEHICLE_ID = "extra_vehicle_id";
    public static final String EXTRA_VEHICLE_NAME = "extra_vehicle_name";
    public static final String EXTRA_VEHICLE_STATUS = "extra_vehicle_status";
    public static final String EXTRA_VEHICLE_BATTERY = "extra_vehicle_battery";
    public static final String EXTRA_VEHICLE_TYPE = "extra_vehicle_type";
    public static final String EXTRA_VEHICLE_STATION = "extra_vehicle_station";

    public static Intent createIntent(
        Context context,
        String id,
        String name,
        String status,
        int battery,
        String type,
        String stationName
    ) {
        Intent intent = new Intent(context, VehicleDetailActivity.class);
        intent.putExtra(EXTRA_VEHICLE_ID, id);
        intent.putExtra(EXTRA_VEHICLE_NAME, name);
        intent.putExtra(EXTRA_VEHICLE_STATUS, status);
        intent.putExtra(EXTRA_VEHICLE_BATTERY, battery);
        intent.putExtra(EXTRA_VEHICLE_TYPE, type);
        intent.putExtra(EXTRA_VEHICLE_STATION, stationName);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_vehicle_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView vehicleNameView = findViewById(R.id.vehicleName);
        TextView vehicleStatus = findViewById(R.id.vehicleStatus);
        TextView vehicleBattery = findViewById(R.id.vehicleBattery);
        TextView vehicleType = findViewById(R.id.vehicleType);
        TextView vehicleStation = findViewById(R.id.vehicleStation);
        Button reserveButton = findViewById(R.id.buttonReserveVehicle);

        String vehicleName = getIntent().getStringExtra(EXTRA_VEHICLE_NAME);
        String status = getIntent().getStringExtra(EXTRA_VEHICLE_STATUS);
        int battery = getIntent().getIntExtra(EXTRA_VEHICLE_BATTERY, -1);
        String type = getIntent().getStringExtra(EXTRA_VEHICLE_TYPE);
        String stationName = getIntent().getStringExtra(EXTRA_VEHICLE_STATION);

        vehicleNameView.setText(vehicleName != null ? vehicleName : getString(R.string.vehicle_detail_unknown));
        vehicleStatus.setText(getString(R.string.vehicle_status_format, status != null ? status : "?"));
        vehicleBattery.setText(getString(R.string.vehicle_battery_format, Math.max(battery, 0)));
        vehicleType.setText(getString(R.string.vehicle_type_format, type != null ? type : "?"));
        vehicleStation.setText(getString(R.string.vehicle_station_format, stationName != null ? stationName : "?"));

        reserveButton.setOnClickListener(v -> {
            showMessage(getString(R.string.reserve_placeholder_success));
            startActivity(PaymentActivity.createIntent(this));
        });
    }
}
