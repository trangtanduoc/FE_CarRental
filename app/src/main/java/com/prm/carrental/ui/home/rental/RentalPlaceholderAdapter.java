package com.prm.carrental.ui.home.rental;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;

import java.util.Arrays;
import java.util.List;

/**
 * Placeholder data set for rental history.
 */
class RentalPlaceholderAdapter extends RecyclerView.Adapter<RentalPlaceholderAdapter.ViewHolder> {

    private final List<String> rentals = Arrays.asList(
        "Thuê VF e34 - 18/10",
        "Thuê Tesla Model 3 - 12/10"
    );

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(rentals.get(position));
    }

    @Override
    public int getItemCount() {
        return rentals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        void bind(String name) {
            textView.setText(name);
            itemView.setOnClickListener(v -> openDetail(v.getContext(), name));
        }

        private void openDetail(Context context, String rentalTitle) {
            Intent intent = new Intent(context, RentalDetailActivity.class);
            intent.putExtra(RentalDetailActivity.EXTRA_RENTAL_TITLE, rentalTitle);
            context.startActivity(intent);
        }
    }
}
