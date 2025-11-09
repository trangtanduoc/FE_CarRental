package com.prm.carrental.ui.home.station;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.network.model.ApiResponse;
import com.prm.carrental.core.network.model.PagedResponse;
import com.prm.carrental.core.network.model.StationDto;
import com.prm.carrental.core.ui.BaseFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private StationAdapter adapter;
    private Call<ApiResponse<PagedResponse<StationDto>>> currentCall;

    @Override
    protected int layoutId() {
        return R.layout.fragment_station_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressStations);
        emptyView = view.findViewById(R.id.stationEmptyView);
        recyclerView = view.findViewById(R.id.rvStationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new StationAdapter(this::openStationDetail);
        recyclerView.setAdapter(adapter);
        fetchStations();
    }

    private void fetchStations() {
        showLoading(true);
        emptyView.setVisibility(View.GONE);
        currentCall = ServiceLocator.stationsService().getStations(1, 20);
        currentCall.enqueue(new Callback<ApiResponse<PagedResponse<StationDto>>>() {
            @Override
            public void onResponse(
                @NonNull Call<ApiResponse<PagedResponse<StationDto>>> call,
                @NonNull Response<ApiResponse<PagedResponse<StationDto>>> response
            ) {
                if (!isAdded()) {
                    return;
                }
                showLoading(false);
                ApiResponse<PagedResponse<StationDto>> body = response.body();
                if (body == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                if (!body.isSuccess()) {
                    showMessage(extractError(body));
                    return;
                }
                PagedResponse<StationDto> value = body.getValue();
                if (value == null) {
                    showMessage(getString(R.string.error_generic));
                    return;
                }
                List<StationDto> stations = value.getItems();
                adapter.submit(stations);
                if (stations.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(R.string.station_list_title);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(
                @NonNull Call<ApiResponse<PagedResponse<StationDto>>> call,
                @NonNull Throwable t
            ) {
                if (!isAdded()) {
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

    private void openStationDetail(StationDto station) {
        startActivity(StationDetailActivity.createIntent(requireContext(), station.getId(), station.getName()));
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentCall != null) {
            currentCall.cancel();
            currentCall = null;
        }
    }
}
