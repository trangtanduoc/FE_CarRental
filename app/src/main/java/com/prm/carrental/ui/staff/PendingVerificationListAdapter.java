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
 * Placeholder adapter showing fake data.
 */
class PendingVerificationListAdapter extends RecyclerView.Adapter<PendingVerificationListAdapter.ViewHolder> {

    interface Callback {
        void onUserSelected(String name);
    }

    private final List<String> users = Arrays.asList(
        "Nguyễn Văn A",
        "Trần Thị B"
    );

    private final Callback callback;

    PendingVerificationListAdapter(Callback callback) {
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
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        void bind(String name) {
            textView.setText(name);
            itemView.setOnClickListener(v -> callback.onUserSelected(name));
        }
    }
}
