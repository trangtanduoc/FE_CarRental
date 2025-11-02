package com.prm.carrental.ui.staff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

/**
 * Presents vehicles managed by a staff member.
 */
class StationVehiclesAdapter extends RecyclerView.Adapter<StationVehiclesAdapter.ViewHolder> {

    interface Callback {
        void onVehicleSelected(String name);
    }

    private final List<String> vehicles = Arrays.asList(
        "VF e34 - 80% pin",
        "Tesla Model 3 - 65% pin"
    );

    private final Callback callback;

    StationVehiclesAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(vehicles.get(position));
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        void bind(String name) {
            textView.setText(name);
            itemView.setOnClickListener(v -> callback.onVehicleSelected(name));
        }
    }
}
