package com.prm.carrental.ui.staff;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Allows staff to approve or reject renter verification.
 */
public class UserVerificationDetailActivity extends BaseActivity {

    public static final String EXTRA_USER_NAME = "extra_user_name";

    @Override
    protected int layoutId() {
        return R.layout.activity_user_verification_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView userName = findViewById(R.id.userName);
        TextView userEmail = findViewById(R.id.userEmail);
        Button approve = findViewById(R.id.buttonApproveUser);
        Button reject = findViewById(R.id.buttonRejectUser);

        String name = getIntent().getStringExtra(EXTRA_USER_NAME);
        userName.setText(name != null ? name : "Người dùng");
        userEmail.setText("demo@prm-carrental.com");

        approve.setOnClickListener(v -> {
            showMessage("Đã xác thực (demo)");
            finish();
        });
        reject.setOnClickListener(v -> {
            showMessage("Đã từ chối (demo)");
            finish();
        });
    }
}
