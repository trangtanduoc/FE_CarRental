package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Lists vehicles for staff to manage.
 */
public class StationVehiclesActivity extends BaseActivity implements StationVehiclesAdapter.Callback {

    @Override
    protected int layoutId() {
        return R.layout.activity_station_vehicles;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.stationVehiclesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StationVehiclesAdapter(this));
    }

    @Override
    public void onVehicleSelected(String name) {
        Intent intent = new Intent(this, VehicleManagementActivity.class);
        intent.putExtra(VehicleManagementActivity.EXTRA_VEHICLE_NAME, name);
        startActivity(intent);
    }
}
