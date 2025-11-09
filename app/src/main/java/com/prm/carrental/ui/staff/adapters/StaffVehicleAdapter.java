package com.prm.carrental.ui.staff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prm.carrental.R;
import com.prm.carrental.core.network.model.VehicleDto;

import java.util.List;

public class StaffVehicleAdapter extends RecyclerView.Adapter<StaffVehicleAdapter.StaffVehicleViewHolder>{
    private Context context;
    private List<VehicleDto> vehicleList;
    private StaffVehicleAdapter.OnStaffVehicleClickListener listener;

    // ===== THÊM INTERFACE NÀY =====
    public interface OnStaffVehicleClickListener {
        void onStaffVehicleSelected(VehicleDto user);
        //void onEditStation(VehicleDto station);
        //void onDeleteStation(VehicleDto station);
    }

    // ===== SỬA CONSTRUCTOR - THÊM THAM SỐ listener =====
    public StaffVehicleAdapter(Context context, List<VehicleDto> vehicleList, StaffVehicleAdapter.OnStaffVehicleClickListener listener) {
        this.context = context;
        this.vehicleList = vehicleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StaffVehicleAdapter.StaffVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vehicle, parent, false);
        return new StaffVehicleAdapter.StaffVehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffVehicleAdapter.StaffVehicleViewHolder holder, int position) {
        VehicleDto vehicle = vehicleList.get(position);
        holder.tvVehicleBattery.setText(String.valueOf(vehicle.getBatteryLevel()));
        holder.tvVehicleStatus.setText(vehicle.getStatus());
        holder.tvType.setText(
                switch (vehicle.getType()) {
                    case "1" -> "Xe máy";
                    case "2" -> "Ô tô";
                    case "3" -> "Loại khác";
                    default -> "Không xác định";
                }
        );

        holder.tvVehiclePlate.setText(vehicle.getPlateNumber());
        holder.btnDelete.setVisibility(View.GONE);

        // ===== THÊM CLICK LISTENER =====
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStaffVehicleSelected(vehicle);
            }
        });

        Glide.with(context)
                .load(vehicle.getImageUrl())
                .placeholder(R.drawable.ic_vehicle)
                .into(holder.imgVehicle);

        // ===== THÊM CLICK EDIT LISTENER =====
//        holder.btnEdit.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onEditStation(vehicle);
//            }
//        });

        // ===== THÊM CLICK DELETE LISTENER =====
//        holder.btnDelete.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onDeleteStation(vehicle);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    // ===== THÊM METHOD UPDATE DATA =====
    public void updateData(List<VehicleDto> newVehicleList) {
        this.vehicleList.clear();
        this.vehicleList.addAll(newVehicleList);
        notifyDataSetChanged();
    }

    public static class StaffVehicleViewHolder extends RecyclerView.ViewHolder {
        ImageView imgVehicle;
        TextView tvVehiclePlate, tvType, tvVehicleStatus, tvVehicleBattery;
        ImageButton btnDelete;

        public StaffVehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVehicle = itemView.findViewById(R.id.imgVehicle);
            tvVehiclePlate = itemView.findViewById(R.id.tvVehiclePlate);
            tvType = itemView.findViewById(R.id.tvType);
            tvVehicleStatus = itemView.findViewById(R.id.tvVehicleStatus);
            tvVehicleBattery = itemView.findViewById(R.id.tvVehicleBattery);
            //btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
