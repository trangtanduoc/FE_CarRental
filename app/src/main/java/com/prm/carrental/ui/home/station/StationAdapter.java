package com.prm.carrental.ui.home.station;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.model.StationDto;

import java.util.ArrayList;
import java.util.List;

class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    interface Callback {
        void onStationSelected(StationDto station);
    }

    private final List<StationDto> data = new ArrayList<>();
    private final Callback callback;

    StationAdapter(Callback callback) {
        this.callback = callback;
    }

    void submit(List<StationDto> stations) {
        data.clear();
        if (stations != null) {
            data.addAll(stations);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_station, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position), callback);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView address;
        private final TextView availability;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stationName);
            address = itemView.findViewById(R.id.stationAddress);
            availability = itemView.findViewById(R.id.stationAvailability);
        }

        void bind(StationDto station, Callback callback) {
            name.setText(station.getName());
            address.setText(station.getAddress());
            availability.setText(
                itemView.getContext().getString(R.string.station_available_vehicles, station.getAvailableVehicles())
            );
            itemView.setOnClickListener(v -> callback.onStationSelected(station));
        }
    }
}
