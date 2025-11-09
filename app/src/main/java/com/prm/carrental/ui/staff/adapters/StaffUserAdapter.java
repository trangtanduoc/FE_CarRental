package com.prm.carrental.ui.staff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prm.carrental.R;
import com.prm.carrental.core.network.model.UserDto;

import java.util.List;

public class StaffUserAdapter extends RecyclerView.Adapter<StaffUserAdapter.UserViewHolder> {
    private Context context;
    private List<UserDto> userList;
    private OnUserClickListener listener;

    // ===== THÊM INTERFACE NÀY =====
    public interface OnUserClickListener {
        void onUserSelected(UserDto user);
    }

    // ===== SỬA CONSTRUCTOR - THÊM THAM SỐ listener =====
    public StaffUserAdapter(Context context, List<UserDto> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDto user = userList.get(position);
        holder.txtName.setText(user.getFullName());
        holder.txtEmail.setText(user.getEmail());
        holder.txtRole.setText(user.getRole());

        // ===== THÊM CLICK LISTENER =====
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserSelected(user);
            }
        });

        Glide.with(context)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.ic_person)
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ===== THÊM METHOD UPDATE DATA =====
    public void updateData(List<UserDto> newUserList) {
        this.userList.clear();
        this.userList.addAll(newUserList);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName, txtEmail, txtRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtRole = itemView.findViewById(R.id.txtRole);
        }
    }
}