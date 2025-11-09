package com.prm.carrental.ui.staff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.network.model.StationDto;

import java.util.List;

public class StaffStationAdapter extends RecyclerView.Adapter<StaffStationAdapter.StaffStationViewHolder>{
    private Context context;
    private List<StationDto> stationList;
    private StaffStationAdapter.OnStaffStationClickListener listener;

    // ===== THÊM INTERFACE NÀY =====
    public interface OnStaffStationClickListener {
        void onStaffStationSelected(StationDto station);
        void onEditStation(StationDto station);
        void onDeleteStation(StationDto station);
    }

    // ===== SỬA CONSTRUCTOR - THÊM THAM SỐ listener =====
    public StaffStationAdapter(Context context, List<StationDto> stationList, StaffStationAdapter.OnStaffStationClickListener listener) {
        this.context = context;
        this.stationList = stationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StaffStationAdapter.StaffStationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_station, parent, false);
        return new StaffStationAdapter.StaffStationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffStationAdapter.StaffStationViewHolder holder, int position) {
        StationDto station = stationList.get(position);
        holder.tvStationName.setText(station.getName());
        holder.tvStationAddress.setText(station.getAddress());
        holder.tvStationAvailability.setText(String.valueOf(station.getAvailableVehicles()));

        // ===== THÊM CLICK LISTENER =====
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStaffStationSelected(station);
            }
        });

        // ===== THÊM CLICK EDIT LISTENER =====
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditStation(station);
            }
        });

        // ===== THÊM CLICK DELETE LISTENER =====
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteStation(station);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    // ===== THÊM METHOD UPDATE DATA =====
    public void updateData(List<StationDto> newStationList) {
        this.stationList.clear();
        this.stationList.addAll(newStationList);
        notifyDataSetChanged();
    }

    public static class StaffStationViewHolder extends RecyclerView.ViewHolder {
        TextView tvStationName, tvStationAddress, tvStationAvailability;
        ImageButton btnEdit, btnDelete;

        public StaffStationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationName = itemView.findViewById(R.id.tvStationName);
            tvStationAddress = itemView.findViewById(R.id.tvStationAddress);
            tvStationAvailability = itemView.findViewById(R.id.tvStationAvailability);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
