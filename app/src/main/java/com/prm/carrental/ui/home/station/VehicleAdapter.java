package com.prm.carrental.ui.home.station;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.model.VehicleDto;

import java.util.ArrayList;
import java.util.List;

class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    interface Callback {
        void onVehicleSelected(VehicleDto vehicle);
    }

    private final List<VehicleDto> data = new ArrayList<>();
    private final Callback callback;

    VehicleAdapter(Callback callback) {
        this.callback = callback;
    }

    void submit(List<VehicleDto> vehicles) {
        data.clear();
        if (vehicles != null) {
            data.addAll(vehicles);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_vehicle, parent, false);
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

        private final TextView plate;
        private final TextView status;
        private final TextView battery;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            plate = itemView.findViewById(R.id.vehiclePlate);
            status = itemView.findViewById(R.id.vehicleStatus);
            battery = itemView.findViewById(R.id.vehicleBattery);
        }

        void bind(VehicleDto vehicle, Callback callback) {
            plate.setText(vehicle.getPlateNumber());
            status.setText(
                itemView.getContext().getString(R.string.vehicle_status_format, vehicle.getStatus())
            );
            battery.setText(
                itemView.getContext().getString(R.string.vehicle_battery_format, vehicle.getBatteryLevel())
            );
            itemView.setOnClickListener(v -> callback.onVehicleSelected(vehicle));
        }
    }
}
