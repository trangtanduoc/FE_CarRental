package com.prm.carrental.ui.home.station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.VehicleDto;
import com.prm.carrental.core.ui.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Hosts vehicle list for a selected station.
 */
public class StationDetailActivity extends BaseActivity {

    public static final String EXTRA_STATION_ID = "extra_station_id";
    public static final String EXTRA_STATION_NAME = "extra_station_name";

    private ProgressBar progressBar;
    private VehicleAdapter adapter;
    private RecyclerView recyclerView;
    private Call<ApiResponse<PagedResponse<VehicleDto>>> currentCall;

    public static Intent createIntent(Context context, String stationId, String stationName) {
        Intent intent = new Intent(context, StationDetailActivity.class);
        intent.putExtra(EXTRA_STATION_ID, stationId);
        intent.putExtra(EXTRA_STATION_NAME, stationName);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_station_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView stationName = findViewById(R.id.stationName);
        progressBar = findViewById(R.id.progressVehicles);
        recyclerView = findViewById(R.id.vehicleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VehicleAdapter(this::openVehicleDetail);
        recyclerView.setAdapter(adapter);

        String name = getIntent().getStringExtra(EXTRA_STATION_NAME);
        if (name != null) {
            stationName.setText(name);
        }

        String stationId = getIntent().getStringExtra(EXTRA_STATION_ID);
        if (stationId == null) {
            showMessage(getString(R.string.error_generic));
            finish();
            return;
        }
        fetchVehicles(stationId);
    }

    private void fetchVehicles(String stationId) {
        showLoading(true);
        currentCall = ServiceLocator.stationsService().getVehiclesInStation(stationId, 1, 20);
        currentCall.enqueue(new Callback<ApiResponse<PagedResponse<VehicleDto>>>() {
            @Override
            public void onResponse(
                Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                Response<ApiResponse<PagedResponse<VehicleDto>>> response
            ) {
                showLoading(false);
                ApiResponse<PagedResponse<VehicleDto>> body = response.body();
                if (body == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                if (!body.isSuccess()) {
                    showMessage(extractError(body));
                    return;
                }
                PagedResponse<VehicleDto> value = body.getValue();
                if (value == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                List<VehicleDto> vehicles = value.getItems();
                adapter.submit(vehicles);
                if (vehicles.isEmpty()) {
                    showMessage(getString(R.string.vehicle_list_empty));
                }
            }

            @Override
            public void onFailure(
                Call<ApiResponse<PagedResponse<VehicleDto>>> call,
                Throwable t
            ) {
                if (call.isCanceled()) {
                    return;
                }
                showLoading(false);
                showMessage(getString(R.string.error_network, t.getLocalizedMessage()));
            }
        });
    }

    private String extractError(ApiResponse<?> response) {
        if (response.getErrors().isEmpty()) {
            return getString(R.string.error_generic);
        }
        return response.getErrors().get(0).getMessage();
    }

    private void openVehicleDetail(VehicleDto vehicle) {
        Intent intent = VehicleDetailActivity.createIntent(
            this,
            vehicle.getId(),
            vehicle.getPlateNumber(),
            vehicle.getStatus(),
            vehicle.getBatteryLevel(),
            vehicle.getType(),
            vehicle.getStationName()
        );
        startActivity(intent);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentCall != null) {
            currentCall.cancel();
            currentCall = null;
        }
    }
}
